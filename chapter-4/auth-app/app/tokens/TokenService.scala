package tokens

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.microservices.auth.{Token, TokenStr}
import utils.Contexts

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class TokenService @Inject()(context: Contexts, tokensDao: TokenDao) {
  def createToken(key: String)(implicit exec:ExecutionContext): Future[Token] = {
    val token = generateToken(key)
    tokensDao.createToken(token).map(_ => token)
  }

  def authenticateToken(token: TokenStr, refresh:Boolean)(implicit exec:ExecutionContext): Future[Token] = {
    tokensDao.getToken(token.tokenStr).map{
      case Some(t) =>
        if (t.validTill < System.currentTimeMillis())
          throw new IllegalArgumentException("Token expired.")
        else {
          if(refresh) {
            val max = maxTTL
            tokensDao.updateTTL(token.tokenStr, max)
            Token(t.token, max, t.key)
          }else t
        }
      case None => throw new IllegalArgumentException("Not a valid Token.")
    }
  }


  def dropToken(key:TokenStr)={
    tokensDao.deleteToken(key.tokenStr)
  }


  private def generateToken(key: String) = Token(generateTokenStr, maxTTL, key)

  private def generateTokenStr: TokenStr = TokenStr(UUID.randomUUID().toString)
  private def maxTTL = System.currentTimeMillis() + context.tokenTTL

}
