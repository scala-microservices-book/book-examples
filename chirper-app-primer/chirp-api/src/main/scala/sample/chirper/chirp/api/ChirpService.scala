/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.chirp.api

import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.ServiceCall

trait ChirpService extends Service {

  def addChirp(userId: String): ServiceCall[Chirp, Done]
  
  def getLiveChirps: ServiceCall[LiveChirpsRequest, Source[Chirp, NotUsed]]
  
  def getHistoricalChirps: ServiceCall[HistoricalChirpsRequest, Source[Chirp, NotUsed]]

  override def descriptor(): Descriptor = {
    import Service._

    named("chirpservice").withCalls(
        pathCall("/api/chirps/live/:userId", addChirp _),
        namedCall("/api/chirps/history", getHistoricalChirps _),
        namedCall("/api/chirps/live", getLiveChirps _)
      ).withAutoAcl(true)
  }
}
