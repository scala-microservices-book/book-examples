/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.activity.api

import sample.chirper.chirp.api.Chirp

import akka.stream.scaladsl.Source

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service

trait ActivityStreamService extends Service {

  def getLiveActivityStream(userId: String): ServiceCall[NotUsed, Source[Chirp, NotUsed]]

  def getHistoricalActivityStream(userId: String): ServiceCall[NotUsed, Source[Chirp, NotUsed]]

  override def descriptor(): Descriptor = {
    import Service._

    named("activityservice").withCalls(
        pathCall("/api/activity/:userId/live", getLiveActivityStream _),
        pathCall("/api/activity/:userId/history", getHistoricalActivityStream _)
      ).withAutoAcl(true)
  }
}
