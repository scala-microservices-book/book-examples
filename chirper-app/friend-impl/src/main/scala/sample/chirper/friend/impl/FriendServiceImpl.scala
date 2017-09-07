/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraReadSide
import sample.chirper.friend.api.{CreateUser, FriendId, FriendService, User}

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext

class FriendServiceImpl(persistentEntities: PersistentEntityRegistry,  readSide: CassandraReadSide,
                        db: CassandraSession)(implicit ec: ExecutionContext) extends FriendService {

 override def getUser(id: String): ServiceCall[NotUsed, User] = {
    request =>
      friendEntityRef(id).ask(GetUser())
        .map(_.user.getOrElse(throw NotFound(s"user $id not found")))
  }

  override def createUser(): ServiceCall[CreateUser, Done] = {
    request =>
        friendEntityRef(request.userId).ask(CreateUserCommand(User(request)))
  }

  override def addFriend(userId: String): ServiceCall[FriendId, Done] = {
    request =>
      friendEntityRef(userId).ask(AddFriend(request.friendId))
  }

  override def getFollowers(id: String): ServiceCall[NotUsed, Seq[String]] = {
    req =>
    {
      db.selectAll("SELECT * FROM follower WHERE userId = ?", id).map { jrows =>
        val rows = jrows.toVector
        rows.map(_.getString("followedBy"))
      }
    }
  }


  private def friendEntityRef(userId: String) =
    persistentEntities.refFor[FriendEntity](userId)
}