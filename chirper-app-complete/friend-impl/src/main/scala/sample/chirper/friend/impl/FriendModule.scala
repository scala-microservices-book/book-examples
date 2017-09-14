/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.friend.impl


import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.friend.api.FriendService

import scala.collection.immutable

abstract class FriendModule (context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
  with CassandraPersistenceComponents
  with LagomKafkaComponents
{

  persistentEntityRegistry.register(wire[FriendEntity])
//  readSide.register(wire[FriendEventProcessor])
  override def jsonSerializerRegistry = FriendSerializerRegistry

  override lazy val lagomServer: LagomServer = serverFor[FriendService](wire[FriendServiceImpl])
}

class FriendApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new FriendModule(context) with LagomDevModeComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new FriendModule(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[FriendService])
}

/**
  * This is telling Lagom to use the below serializers for persistence. One could use some other format
  * apart from Json, and if you do, you need to declare it here.
  * Lagom by default provides support for json.
  */
object FriendSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
      JsonSerializer[CreateUserCommand],
      JsonSerializer[GetUser],
      JsonSerializer[GetUserReply],
      JsonSerializer[AddFriend],
      JsonSerializer[UserCreated],
      JsonSerializer[FriendAdded],
      JsonSerializer[FriendState]
    )

}
