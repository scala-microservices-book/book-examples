/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl

import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import sample.chirper.friend.api.{CreateUser, FriendId, FriendService, User}

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

class FriendServiceImpl ()(implicit ec: ExecutionContext) extends FriendService {

  val userMap = new ConcurrentHashMap[String, User]()

  val friendsMap = new ConcurrentHashMap[String, ConcurrentLinkedQueue[User]]()

  override def getUser(id: String): ServiceCall[NotUsed, User] = {
    request =>
      val user = userMap.get(id)
        if(user == null)
          throw NotFound(s"user $id not found")
        else {
          Future.successful(getUser(user.userId, user.name))
        }
  }

  override def createUser(): ServiceCall[CreateUser, Done] = {
    request =>
      val alreadyExists = userMap.get(request.userId)
      if(alreadyExists != null){
        throw NotFound(s"user $request already exists")
      }

      val user = User(request)
      userMap.put(request.userId, user)
      val friends = new ConcurrentLinkedQueue[User]()

      friendsMap.put(user.userId, friends)
      Future.successful(Done)
  }

  override def addFriend(userId: String): ServiceCall[FriendId, Done] = {
    request =>
      val user = userMap.get(userId)

      if(user == null)
        throw NotFound(s"user $userId not found")
      else {
          val friendsList = friendsMap.get(userId)
          val friend = userMap.get(request.friendId)
          friendsList.add(friend)
          Future.successful(Done)
        }
      }


  override def getFollowers(id: String): ServiceCall[NotUsed, Seq[String]] = {
    req =>
      {
        import scala.collection.JavaConverters._

        val user = userMap.get(id)
        if(user == null)
          throw NotFound(s"user $id not found")
        else {
          Future.successful(getUser(user.userId, user.name).friends)
        }
      }
  }

  private def getUser(userId: String, name:String): User ={
    import scala.collection.JavaConverters._

    User(userId, name, friendsMap.get(userId).asScala.toList.map(x => x.userId))
  }
}