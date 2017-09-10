package tokens

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.microservices.auth.{Token, TokenStr}
import utils.Contexts

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class TokenService @Inject()(context: Contexts, tokensDao: TokenDao) {
  /**
    * Creates a token based on the key provided. If there was already a token generated for the key and is valid, then the same token is returned
    * Else a new token is generated and returned
    * @param key key for example user email
    * @return
    */
  def createToken(key: String)(implicit exec:ExecutionContext): Future[Token] = {
    tokensDao.getTokenFromkey(key).flatMap {
      case Some(token) =>
        if(token.validTill <= System.currentTimeMillis()){
          dropToken(token.token)
          val newToken: Token = generateToken(key)
          tokensDao.createToken(newToken).map(_ => newToken)
        } else{
          Future(token)
        }
      case None =>
        val newToken = generateToken(key)
        tokensDao.createToken(newToken).map(_ => newToken)
    }
  }

  /**
    * verifies if its a valid token. Returns a future completed with token if so. Else the returned future completes with an exception
    */
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
