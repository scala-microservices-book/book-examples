package com.microservices.auth

import play.api.libs.functional.syntax.unlift
import play.api.libs.json._


case class User(email: String, password: String)

object User {
  implicit val userJS = Json.format[User]
}


case class TokenStr(tokenStr: String)

case class Token(token: TokenStr, validTill: Long, key: String)

object TokenStr {
  implicit val tokenSTRJS = Json.format[TokenStr]
}

object Token {
  implicit val tokenJS = Json.format[Token]
}

case class SimpleMessage(message: String)

object SimpleMessage {
  implicit val formatSR = Json.format[SimpleMessage]
}

/**
  * In all the service calls we return `com.microservices.auth.ResponseObj` object. It has two sub classes which represent
  * a success (with the body) or a failure (with a message).
  *
  * A sample success call: {"isSuccess":true,"message":{"tokenStr":"677678f7-5dc9-4236-a254-c067b0662e8c"}}
  * A sample failure call: {"isSuccess":true,"message":"username/password mismatch"}
  *
  * The reason we wrap it with `ResponseObj` is that when the json is responded back to the user, the caller can take decision
  * based on the `isSuccess` flag.
  */
sealed abstract class ResponseObj(val isSuccess: Boolean)

case class SuccessRes[T](message: T) extends ResponseObj(true)

case class FailureRes(message: String) extends ResponseObj(false)

object SuccessRes {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit def reads[T: Reads]: Reads[SuccessRes[T]] = (
    (JsPath \ "isSuccess").read[Boolean] and
      (JsPath \ "message").read[T]
    ) ((x, y) => SuccessRes(y))

  implicit def writes[T: Writes]: Writes[SuccessRes[T]] = (
    (JsPath \ "isSuccess").write[Boolean] and
      (JsPath \ "message").write[T]
    ) (x => (true, x.message))
}

object FailureRes {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit def reads: Reads[FailureRes] = (
    (JsPath \ "isSuccess").read[Boolean] and
      (JsPath \ "message").read[String]
    ) ((x, y) => FailureRes(y))

  implicit def writes: Writes[FailureRes] = (
    (JsPath \ "isSuccess").write[Boolean] and
      (JsPath \ "message").write[String]
    ) (x => (false, x.message))
}

object ResponseObj {
  def asSuccess[T: Writes](message: T) = Json.toJson(SuccessRes(message))
  def asFailure(message: String) = Json.toJson(FailureRes(message))

  implicit def reads[T:Reads] = new Reads[ResponseObj] {
    override def reads(json: JsValue) =
      if(json.validate(SuccessRes.reads[T]).isSuccess)
        json.validate(SuccessRes.reads[T])
      else if(json.validate(FailureRes.reads).isSuccess) {
        json.validate(FailureRes.reads)
      }
      else {
        throw new RuntimeException("no valid reads found: "+json+". reads called with: "+implicitly[Reads[T]]+". ")
      }
  }

}