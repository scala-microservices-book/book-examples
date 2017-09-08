package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.AggregateEvent
import com.lightbend.lagom.scaladsl.persistence.AggregateEventTag
import java.time.Instant

import play.api.libs.json.Json

object FriendEvent {
  val Tag = AggregateEventTag[FriendEvent]
}
sealed trait FriendEvent extends AggregateEvent[FriendEvent] {
  override def aggregateTag(): AggregateEventTag[FriendEvent] = FriendEvent.Tag
}

case class UserCreated(userId: String, name: String, timestamp: Instant = Instant.now()) extends FriendEvent

case class FriendAdded(userId: String, friendId: String, timestamp: Instant = Instant.now()) extends FriendEvent

object UserCreated{
  implicit val format = Json.format[UserCreated]
}
object FriendAdded{
  implicit val format = Json.format[FriendAdded]
}
