/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import akka.Done
import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import sample.chirper.friend.api.User


sealed trait FriendCommand

case class CreateUserCommand(user: User) extends PersistentEntity.ReplyType[Done] with FriendCommand
case class GetUser() extends PersistentEntity.ReplyType[GetUserReply] with FriendCommand

case class GetUserReply(user: Option[User])
case class AddFriend(friendUserId: String) extends PersistentEntity.ReplyType[Done] with FriendCommand

object CreateUserCommand{
  implicit val format = Json.format[CreateUserCommand]
}
object GetUser{
  implicit val strictReads = Reads[GetUser](json => json.validate[JsObject].filter(_.values.isEmpty).map(_ => GetUser()))
  implicit val writes = OWrites[GetUser](_ => Json.obj())
}
object GetUserReply{
  implicit val format = Json.format[GetUserReply]
}
object AddFriend{
  implicit val format = Json.format[AddFriend]
}