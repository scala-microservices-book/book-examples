/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.load.api

import com.fasterxml.jackson.annotation.JsonIgnore
import play.api.libs.json.{Format, Json}

case class TestParams @JsonIgnore()(
  users: Int,
  friends: Int,
  chirps: Int,
  clients: Int,
  parallelism: Int,
  userIdPrefix: Option[String]
  ){

  def this() = this(1000, 10, 100000, 10, 10, Option.empty)
}

object TestParams{
  implicit val format:Format[TestParams] =  Json.format
}
