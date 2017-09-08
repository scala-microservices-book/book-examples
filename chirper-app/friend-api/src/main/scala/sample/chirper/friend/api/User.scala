/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.api

import java.time.Instant

import play.api.libs.json.{JsValue, Json, Reads, Writes}

import scala.collection.immutable.Seq

case class CreateUser(userId: String, name: String)

case class User(userId: String, name: String, friends: Seq[String]) {
  def this(userId: String, name: String) = this(userId, name, Seq.empty)
}

sealed trait KFriendMessage

case class KUserCreated(userId: String, name: String, timestamp: Instant = Instant.now()) extends KFriendMessage

case class KFriendAdded(userId: String, friendId: String, timestamp: Instant = Instant.now()) extends KFriendMessage


object CreateUser {
  implicit val createUserJson = Json.format[CreateUser]
}

object User {
  implicit val userJson = Json.format[User]

  def apply(createUser: CreateUser): User = User(createUser.userId, createUser.name, Seq())
  //  def apply (userId: String, name: String, friends: Seq[String]) = new User(userId, name, friends)
}

object KFriendMessage{
  implicit val writes = new Writes[KFriendMessage]{
    override def writes(o: KFriendMessage) = o match {
      case x: KUserCreated => KUserCreated.format.writes(x)
      case x: KFriendAdded => KFriendAdded.format.writes(x)
    }
  }

  implicit val reads = new Reads[KFriendMessage]{
    override def reads(json: JsValue) =
      if(json.validate(KUserCreated.format).isSuccess)
        json.validate(KUserCreated.format)
      else if(json.validate(KFriendAdded.format).isSuccess)
        json.validate(KFriendAdded.format)
      else throw new RuntimeException("no valid format found")
  }
}

object KUserCreated {
  implicit val format = Json.format[KUserCreated]
}

object KFriendAdded {
  implicit val format = Json.format[KFriendAdded]
}
