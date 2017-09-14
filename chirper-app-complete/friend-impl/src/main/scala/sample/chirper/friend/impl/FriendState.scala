package sample.chirper.friend.impl

import play.api.libs.json.Json
import sample.chirper.friend.api.User


case class FriendState(user: Option[User]) {
  def addFriend(friendUserId: String): FriendState = user match {
    case None => throw new IllegalStateException("friend can't be added before user is created")
    case Some(x) =>
      val newFriends = x.friends :+ friendUserId
      FriendState(Some(x.copy(friends = newFriends)))
  }
}

object FriendState {
  def apply(user: User): FriendState = FriendState(Option(user))

  def emptyState: FriendState = FriendState(None)

  implicit val format = Json.format[FriendState]
}
