package sample.chirper.friend.impl


import java.util.UUID

import akka.Done

import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import scala.concurrent.{ExecutionContext, Future}

class FriendEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[FriendEvent] {

  @volatile private var writeFollowers: PreparedStatement = null // initialized in prepare
  @volatile private var writeOffset: PreparedStatement = null // initialized in prepare


  def prepare(session: CassandraSession) = {
    for{
      _ <- prepareCreateTables(session)
      _ <- prepareWriteFollowers(session)
      _ <- prepareWriteOffset(session)
    } yield selectOffset(session)
  }

  private def prepareCreateTables(session: CassandraSession) = {
    for {
      _ <- session.executeCreateTable(
        """CREATE TABLE IF NOT EXISTS follower (
          userId text, followedBy text,
          PRIMARY KEY (userId, followedBy))""")
      _ <- session.executeCreateTable(
        """CREATE TABLE IF NOT EXISTS friend_offset (
          partition int, offset timeuuid,
          PRIMARY KEY (partition))""")

    } yield Done
  }

  private def prepareWriteFollowers(session: CassandraSession) = {
    val statement = session.prepare("INSERT INTO follower (userId, followedBy) VALUES (?, ?)")
    statement.map(ps => {
      this.writeFollowers = ps
      Done
    })
  }

  private def prepareWriteOffset(session: CassandraSession) = {
    val statement = session.prepare("INSERT INTO friend_offset (partition, offset) VALUES (1, ?)")
    statement.map(ps => {
      this.writeOffset = ps
      Done
    })
  }

  private def selectOffset(session: CassandraSession) = {
    val select = session.selectOne("SELECT offset FROM friend_offset WHERE partition=1")
    select.map { maybeRow => maybeRow.map[UUID](_.getUUID("offset")) }
  }

//  override def defineEventHandlers(builder: EventHandlersBuilder): EventHandlers = {
//    builder.setEventHandler(classOf[FriendAdded], processFriendChanged)
//    builder.build()
//  }

  private def processFriendChanged(event: FriendAdded, offset: UUID) = {
    val bindWriteFollowers = writeFollowers.bind()
    bindWriteFollowers.setString("userId", event.friendId)
    bindWriteFollowers.setString("followedBy", event.userId)
    val bindWriteOffset = writeOffset.bind(offset)
    Future.successful(List(bindWriteFollowers, bindWriteOffset))
  }


  override def buildHandler() = {
  readSide.builder[FriendEvent]("friendEventOffset")
    .setGlobalPrepare(() => prepareCreateTables(session))
    .setPrepare(_ => prepareWriteFollowers(session))
    .setEventHandler[FriendAdded]((e: EventStreamElement[FriendAdded]) => processFriendChanged(e.event, UUID.fromString(e.entityId)))
      .build()
}

  override def aggregateTags = Set(FriendEvent.Tag)
}