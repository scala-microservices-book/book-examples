package com.microservices.search

import play.api.libs.json.Json

sealed abstract class Element

case class SearchQuery(tags: Option[String], city: Option[String]) extends Element
object SearchQuery{
  implicit val format = Json.format[SearchQuery]
}
