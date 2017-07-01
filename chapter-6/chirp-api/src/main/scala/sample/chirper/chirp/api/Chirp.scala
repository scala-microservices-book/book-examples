/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.chirp.api

import java.time.Instant
import java.util.UUID

import com.fasterxml.jackson.annotation.JsonIgnore
import play.api.libs.json.{JsPath, Json, OFormat, Reads}
import play.api.libs.functional.syntax._ // Combinator syntax

case class Chirp (userId: String, message: String, @JsonIgnore timestamp: Instant,@JsonIgnore uuid: String) {
  def this(userId: String, message: String) =
    this(userId, message, Chirp.defaultTimestamp, Chirp.defaultUUID)
}

object Chirp {
  implicit object ChirpOrdering extends Ordering[Chirp] {
    override def compare(x: Chirp, y: Chirp): Int = x.timestamp.compareTo(y.timestamp)
  }

  def apply(userId: String, message: String, timestamp: Option[Instant], uuid: Option[String]): Chirp =
    new Chirp(userId, message, timestamp.getOrElse(defaultTimestamp), uuid.getOrElse(defaultUUID))

  private def defaultTimestamp = Instant.now()
  private def defaultUUID = UUID.randomUUID().toString

  implicit val chirpRead: Reads[Chirp] = (
    (JsPath \ "userId").read[String] and
      (JsPath \ "message").read[String]
  )((userId, message) => Chirp(userId, message, None, None))

  implicit val chirpWrite = Json.writes[Chirp]
}