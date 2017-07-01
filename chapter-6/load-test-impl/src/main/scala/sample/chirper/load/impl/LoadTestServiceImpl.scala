/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.load.impl

import java.time.{LocalDate, ZoneOffset}
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.LoggerFactory
import sample.chirper.activity.api.ActivityStreamService
import sample.chirper.chirp.api.{Chirp, ChirpService}
import sample.chirper.friend.api.{FriendId, FriendService, User}
import sample.chirper.load.api.{LoadTestService, TestParams}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class LoadTestServiceImpl @Inject() (
    chirpService: ChirpService,
    friendService: FriendService,
    activityService: ActivityStreamService)(implicit mat: Materializer) extends LoadTestService {
  // Needed to convert some Scala types to Java

  private val log = LoggerFactory.getLogger(classOf[LoadTestServiceImpl])

  // to create "unique" user ids we prefix them with this, convenient
  // to not have overlapping user ids when running in dev mode
  private val runSeq = new AtomicLong((System.currentTimeMillis()
    - LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli) / 1000)

  override def startLoad(): ServiceCall[NotUsed, Source[String, NotUsed]] = {
    _ => Future.successful(load(new TestParams()))
  }

  override def startLoadHeadless(): ServiceCall[TestParams, NotUsed] = {
    params =>
      {
        load(params).runWith(Sink.ignore)
        Future.successful(NotUsed)
      }
  }

  private def load(params: TestParams): Source[String, NotUsed] = {
    val runSeqNr = runSeq.incrementAndGet()
    val userIdPrefix = params.userIdPrefix.getOrElse(s"user-$runSeqNr-")

    log.info(s"Starting load with parameters: $params, users prefixed with: $userIdPrefix")
    val userNumbers = Source(1 to params.users)
    val users: Source[User, NotUsed] = userNumbers.map(n => new User(userIdPrefix + n, userIdPrefix.toUpperCase() + n))
    val createdUsers = users
      .mapAsync(params.parallelism) (user => friendService.createUser().invoke(user))
      .via(summary("created users"))

    val friendPairs = userNumbers.mapConcat(n => {
      (1 to params.friends).map(i => n -> (n + i))
    })

    val chirpCount = new AtomicLong()
    val addedFriends = friendPairs.mapAsyncUnordered(params.parallelism) (pair => {
      val invoked = friendService.addFriend(userIdPrefix + pair._1).invoke(FriendId(userIdPrefix + pair._2))
      // start clients when last friend association has been created
      if (params.users == pair._1 && (params.users + params.friends) == pair._2)
        invoked.thenAccept(a => startClients(params.clients, userIdPrefix, chirpCount, runSeqNr))
      invoked
    }).via(summary("added friends"))

    val chirpNumbers = Source(1 to params.chirps)
    val chirps = chirpNumbers.map(n => {
      val userId = userIdPrefix + (n % params.users)
      val message = "Hello " + n + " from " + userId
      new Chirp(userId, message)
    })

    val postedChirps = chirps.mapAsyncUnordered(params.parallelism) (chirp => {
      chirpService.addChirp(chirp.userId).invoke(chirp)
    }).via(summary("posted chirp"))

    val writes = Source(List(createdUsers, addedFriends, postedChirps))
      .flatMapConcat(s => s)

    val interval = FiniteDuration(5, TimeUnit.SECONDS)
    val clientsThroughput = Source.tick(interval, interval, "tick")
      .scan(Throughput(System.nanoTime(), System.nanoTime(), 0, 0))((t, tick) => {
        val now = System.nanoTime()
        val totalCount = chirpCount.get()
        val count = totalCount - t.totalCount
        Throughput(t.endTime, now, count, totalCount)
      })
      .filter(_.throughput > 0.0)
      .map(t => s"client throughput ${String.format("%.2f", Double.box(t.throughput))} chirps/s from " +
        s"${params.clients}  clients (total consumed: ${t.totalCount} chirps)")

    val output = Source(List(writes, clientsThroughput))
      .flatMapMerge(2, s => s)
      .map(s => {
        log.info(s)
        s
      }).map(s => {
        if (runSeq.get() != runSeqNr) {
          val msg = "New test started, stopping previous"
          log.info(msg)
          throw new RuntimeException(msg)
        }
        s
      })

    output
  }

  private def summary(title: String): Flow[NotUsed, String, NotUsed] = {
    Flow[NotUsed]
      .scan(0)((count, elem) => count + 1)
      .drop(1)
      .groupedWithin(1000, FiniteDuration(1, TimeUnit.SECONDS))
      .map(list => list.last)
      .map(c => title + ": " + c)
  }

  private def startClients(numberOfClients: Int, userIdPrefix: String, chirpCount: AtomicLong, runSeqNr: Long): Unit = {
    log.info("starting " + numberOfClients + " clients for users prefixed with " + userIdPrefix)
    for (n <- 1 to numberOfClients) {
      activityService.getLiveActivityStream(userIdPrefix + n).invoke().thenAccept(src => {
        src.map(chirp =>
          if (runSeq.get() != runSeqNr) throw new RuntimeException("New test started, stopping previous clients")
          else chirp
        ).runForeach(chirp => chirpCount.incrementAndGet())
      })
    }
  }
}
