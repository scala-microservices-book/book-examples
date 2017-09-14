package com.scalamicroservices.rec.impl

import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}

import akka.Done
import akka.stream.scaladsl.Flow
import org.slf4j.LoggerFactory
import sample.chirper.friend.api.{FriendService, KFriendAdded, KFriendMessage, KUserCreated}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class receives live stream of messages from the topic. The message could be:
  *   - A new user created or
  *   - A user has added another user as friend
  *
  * This class can then model this information so that it can recommend friends when a service call is made.
  * It could internally model these friends relationship graphs in its best suited model (may be Neo4J graph database).
  * For the sake of simplicity, we calculate this in-memory and respond to users.
  */
class FriendRecServiceImpl(friendService: FriendService)(implicit ex: ExecutionContext) extends FriendRecService {

  val log = LoggerFactory.getLogger(getClass)

  val userMap = new ConcurrentHashMap[String, KUserCreated]()
  val allFriends = new ConcurrentLinkedQueue[KFriendAdded]()

  friendService.friendsTopic.subscribe
    .atLeastOnce(
      Flow[KFriendMessage].map { msg =>
        log.info("KMessage received " + msg)
        msg match {
          case x: KUserCreated => userMap.put(x.userId, x)
          case y: KFriendAdded => allFriends.add(y)
        }
        Done
      }
    )

  override def getFriendRecommendation(userId: String) = {
    request =>
      val ans = getFriends(userId)
        .flatMap(firstLevelFriend => {
          getFriends(firstLevelFriend)
        })
          .filter(ans => ans != userId) //so that it does not recommend itself
      Future.successful(ans)
  }

  private def getFriends(userId: String): Seq[String] ={
    import scala.collection.JavaConversions._
    allFriends.filter(all => all.userId == userId).map(x => x.friendId).toList
  }
}
