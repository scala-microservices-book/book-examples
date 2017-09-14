package com.scalamicroservices.rec.impl

import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService


abstract class FriendRecModule (context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with LagomKafkaClientComponents
{
  lazy val friendService: FriendService = serviceClient.implement[FriendService]
  override lazy val lagomServer: LagomServer = serverFor[FriendRecService](wire[FriendRecServiceImpl])
}

class FriendRecApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new FriendRecModule(context) with LagomDevModeComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FriendRecModule(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FriendRecService])
}
