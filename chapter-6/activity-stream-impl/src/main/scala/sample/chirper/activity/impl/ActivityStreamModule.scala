/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.activity.impl

import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.activity.api.ActivityStreamService
import sample.chirper.chirp.api.ChirpService
import sample.chirper.friend.api.FriendService


abstract class ActivityStreamModule (context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {
  lazy val friendService = serviceClient.implement[FriendService]
  lazy val chirpService = serviceClient.implement[ChirpService]

  override lazy val lagomServer = serverFor[ActivityStreamService](wire[ActivityStreamServiceImpl])
//      bindServices(serviceBinding(classOf[ActivityStreamService], classOf[ActivityStreamServiceImpl]))
//      bindClient(classOf[FriendService])
//      bindClient(classOf[ChirpService])

}


class ActivityStreamApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ActivityStreamModule(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
  //    new FriendModule(context) with ConductRApplicationComponents
    new ActivityStreamModule(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[FriendService]
  )
}


