package com.microservices.search

import play.api.libs.json.Json

abstract class SUserResult{
  require(score >= 0 && score <= 1, s"score must be in range of [0-1]. passed: $score")

  /**
    * value in range of 0-1 absolute
    */
  def score: Float
  def tag: String
  def location: String
}

case class SearchFilter(location: Option[String], tag: Option[String])
object SearchFilter{
  implicit val format = Json.format[SearchFilter]
}

case class SOUser(id:Long, name:String, soAccountId: Long, aboutMe:String, soLink:String="#", location:String)
object SOUser{
  implicit val soUserJSON = Json.format[SOUser]
}

case class SOTag(id:Int, name:String)

object SOTag{
  implicit val soTagJSON = Json.format[SOTag]
}


case class SoUserScore(user:SOUser, map: Map[SOTag, Int])

case class SOSearchResult(override val score:Float, soTag: SOTag, soUser: SOUser) extends SUserResult {
  override val location = soUser.location
  override val tag = soTag.name
}

object SOSearchResult{
  implicit val soSearchResultJSON = Json.format[SOSearchResult]
}

