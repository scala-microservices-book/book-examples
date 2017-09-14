package com.scalamicroservices.rec.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait FriendRecService extends Service{

  def getFriendRecommendation(userId: String): ServiceCall[NotUsed, Seq[String]]

  override def descriptor: Descriptor = {
    import Service._

    named("friendRecService").withCalls(
      pathCall("/api/friends/rec/:id", getFriendRecommendation _)
    ).withAutoAcl(true)
  }
}
