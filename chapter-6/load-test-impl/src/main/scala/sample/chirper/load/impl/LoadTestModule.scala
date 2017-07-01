///*
// * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
// */
//package sample.chirper.load.impl
//
//import com.google.inject.AbstractModule
//import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomServer}
//import play.api.libs.ws.ahc.AhcWSComponents
//import sample.chirper.activity.api.ActivityStreamService
//import sample.chirper.chirp.api.ChirpService
//import sample.chirper.friend.api.FriendService
//import sample.chirper.load.api.LoadTestService
//import com.softwaremill.macwire._
//
//abstract class LoadTestModule (context: LagomApplicationContext)
//  extends LagomApplication(context)
//    with AhcWSComponents{
//
////  lazy val friendService = wire[FriendService]
//  lazy val chirpService = wire[ChirpService]
//  lazy val activityStreamService = wire[ActivityStreamService]
//
//
//  override lazy val lagomServer = serverFor[LoadTestService](wire[LoadTestServiceImpl])
////    bindServices(serviceBinding(classOf[LoadTestService], classOf[LoadTestServiceImpl]))
////    bindClient(classOf[FriendService])
////    bindClient(classOf[ChirpService])
////    bindClient(classOf[ActivityStreamService])
//
//}
