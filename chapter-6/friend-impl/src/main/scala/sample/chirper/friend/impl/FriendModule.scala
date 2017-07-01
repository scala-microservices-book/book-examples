/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl


import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService

abstract class FriendModule (context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {
  override lazy val lagomServer = serverFor[FriendService](wire[FriendServiceImpl])
}

class FriendApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FriendModule(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
  //    new FriendModule(context) with ConductRApplicationComponents
    new FriendModule(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[FriendService]
  )
}

