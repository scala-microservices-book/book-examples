package tokens

import javax.inject.{Inject, Singleton}

import com.microservices.auth.{Token, TokenStr}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.GetResult
import utils.Contexts

import scala.concurrent.Future

@Singleton
class TokenDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, contexts: Contexts) extends HasDatabaseConfigProvider[JdbcProfile] {
  implicit val executionContext = contexts.cpuLookup

  import profile.api._

  implicit val getTokenResult = GetResult(r => Token(TokenStr(r.nextString), r.nextLong(), r.nextString))


  def getToken(token:String): Future[Option[Token]] = {
    db.run(sql"select token, validTill,key from tokens where token = $token".as[Token].headOption)
  }

  def getTokenFromkey(key:String): Future[Option[Token]] = {
    db.run(sql"select token, validTill,key from tokens where key = $key".as[Token].headOption)
  }

  def createToken(token:Token): Future[Int] = {
    db.run(sqlu"insert into tokens (key, token, validTill) values (${token.key}, ${token.token.tokenStr}, ${token.validTill})")
  }

  def deleteToken(token: String)={
    db.run(sqlu"delete from tokens where token=${token}")
  }

  def updateTTL(token: String, till:Long)={
    db.run(sqlu"update tokens set validTill=$till where token=${token}")
  }

}
