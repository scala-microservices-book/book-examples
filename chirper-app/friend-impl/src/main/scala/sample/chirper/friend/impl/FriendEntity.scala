package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class FriendEntity extends PersistentEntity{
  override type Command = this.type
  override type Event = FriendCommand
  override type State = this.type

  override def initialState = ???

  override def behavior = ???
}
