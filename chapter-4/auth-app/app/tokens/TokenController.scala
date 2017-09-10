package tokens

import javax.inject.{Inject, Singleton}

import com.microservices.auth.{ResponseObj, Token, TokenStr}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.Contexts

import scala.concurrent.Future

@Singleton
class TokenController @Inject()(contexts: Contexts, tokenService: TokenService, cc: ControllerComponents) extends AbstractController(cc) {
  implicit val executionContext = contexts.cpuLookup

  /**
    * Refreshes the token for a user with a new token.
    *
    * @param token the token of the user
    * @return
    */
  /*
  curl -X GET http://localhost:5001/v1/tokens/refresh/677678f7-5dc9-4236-a254-c067b0662e8c
   */
  def refreshToken(token: String) = Action.async {
    tokenService.authenticateToken(TokenStr(token), true).map(x => Ok(ResponseObj.asSuccess(x)))
  }

  /**
    * Authenticates a user based on the token provided. Returns the token object if success.
    * Else returns a BadRequest
    *
    * @param token
    * @return
    */
  /*
  Sample call (the token would need to be replaced with your generated token)
  curl -X GET http://localhost:5001/v1/tokens/authenticate/677678f7-5dc9-4236-a254-c067b0662e8c
   */
  def authenticate(token: String) = Action.async {
    tokenService.authenticateToken(TokenStr(token), true)
      .map((x: Token) => Ok(Json.toJson(x)))
      .recoverWith {
        case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure(e.getMessage)))
      }
  }
}
