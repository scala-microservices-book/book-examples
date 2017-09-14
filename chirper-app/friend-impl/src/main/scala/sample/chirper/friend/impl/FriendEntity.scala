package sample.chirper.friend.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import sample.chirper.friend.api.User

class FriendEntity extends PersistentEntity {
  override type Command = FriendCommand
  override type Event = FriendEvent
  override type State = FriendState

  override def initialState: FriendState = FriendState(None)

  override def behavior: ((FriendState) => Actions) = {
    case FriendState(None) =>
      userNotCreated

    case FriendState(Some(x)) =>
      Actions()
        .onCommand[CreateUserCommand, Done] {
        case (CreateUserCommand(user), ctx, state) =>
          ctx.invalidCommand(s"User $x is already created")
          ctx.done
      }
        .orElse(addFriend)
        .orElse(getUserCommand)
  }

  private val getUserCommand = Actions().onReadOnlyCommand[GetUser, GetUserReply] {
    case (GetUser(), ctx, state) => ctx.reply(GetUserReply(state.user))
  }


  val userNotCreated = {
    Actions()
      .onCommand[CreateUserCommand, Done] {
      case (CreateUserCommand(user), ctx, state) =>
        val event = UserCreated(user.userId, user.name)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (UserCreated(userId, name, ts), state) => FriendState(User(userId, name, List()))
    }
      .onReadOnlyCommand[GetUser, GetUserReply] {
      case (GetUser(), ctx, state) =>
        ctx.reply(GetUserReply(None))
    }
  }

  val addFriend = {
    Actions().onCommand[AddFriend, Done] {
      case (AddFriend(id), ctx, FriendState(Some(user))) if user.friends.contains(id) =>
        //user already had the requested command as a friend
        ctx.reply(Done)
        ctx.done
      case (AddFriend(friendUserId), ctx, FriendState(Some(user))) =>
        ctx.thenPersist(FriendAdded(user.userId, friendUserId))(evt => ctx.reply(Done))
    }.onEvent {
      case (FriendAdded(userId, friendId, ts), state) => state.addFriend(friendId)
    }
  }

}
