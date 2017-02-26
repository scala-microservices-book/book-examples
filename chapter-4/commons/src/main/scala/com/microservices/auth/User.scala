package com.microservices.auth



import play.api.libs.json._


case class User(email:String, password:String)

object User{
    implicit val userJS = Json.format[User]
}


case class TokenStr(tokenStr: String)
case class Token(token:TokenStr, validTill: Long, key:String)

object TokenStr{
  implicit val tokenSTRJS = Json.format[TokenStr]
}

object Token{
  import TokenStr.tokenSTRJS
  implicit val tokenJS = Json.format[Token]
}



