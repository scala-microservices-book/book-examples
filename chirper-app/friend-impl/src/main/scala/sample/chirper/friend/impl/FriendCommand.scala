/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import akka.Done
import play.api.libs.json.Json
import sample.chirper.friend.api.User


sealed trait FriendCommand

case class CreateUserCommand(user: User) extends PersistentEntity.ReplyType[Done] with FriendCommand
case class GetUser() extends PersistentEntity.ReplyType[GetUserReply] with FriendCommand

case class GetUserReply(user: Option[User])
case class AddFriend(friendUserId: String) extends PersistentEntity.ReplyType[Done] with FriendCommand

object CreateUser{
  implicit val format = Json.format[CreateUserCommand]
}
object GetUser{
  implicit val format = Json.format[GetUser]
}
object GetUserReply{
  implicit val format = Json.format[GetUserReply]
}
object AddFriend{
  implicit val format = Json.format[AddFriend]
}