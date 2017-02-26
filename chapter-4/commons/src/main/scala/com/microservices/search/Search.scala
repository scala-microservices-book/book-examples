package com.microservices.search

abstract class SUserResult {
  require(score >= 0 && score <= 1, s"score must be in range of [0-1]. passed: $score")

  /**
    * value in range of 0-1 absolute
    */
  def score: Float
  def tag: String
  def location: String
}

case class SearchFilter(location: Option[String], tech: Option[String])
