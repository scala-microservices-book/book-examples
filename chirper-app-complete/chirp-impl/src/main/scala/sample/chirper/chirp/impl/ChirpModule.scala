/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.chirp.impl

import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.pubsub.PubSubComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.chirp.api.ChirpService

import scala.collection.immutable

abstract class ChirpModule(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with PubSubComponents
    with CassandraPersistenceComponents
{
  override lazy val lagomServer = serverFor[ChirpService](wire[ChirpServiceImpl])
  override def jsonSerializerRegistry = new JsonSerializerRegistry {
    override def serializers = List()
  }
}

class ChirpApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ChirpModule(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
//    new ChirpModule(context) with ConductRApplicationComponents
    new ChirpModule(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ChirpService])
}