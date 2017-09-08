package com.scalamicroservices.rec.impl

import akka.Done
import akka.stream.scaladsl.Flow
import sample.chirper.friend.api.{FriendService, KFriendMessage}

import scala.concurrent.{ExecutionContext, Future}

class FriendRecServiceImpl(friendService: FriendService)(implicit ex:ExecutionContext) extends FriendRecService{

  var all = List[KFriendMessage]()

  friendService.friendsTopic.subscribe
    .atLeastOnce(
      Flow[KFriendMessage].map{ msg =>
        // Do somehting with the `msg`
        println("yoyoyo. received: "+msg)
        all = msg :: all
        Done
      }
    )

  override def getFriendRecommendation(userId: String) = {
    request =>
      Future.successful(all.map(x => x.toString))
  }
}
