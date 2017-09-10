package tokens

import javax.inject.{Inject, Singleton}

import com.microservices.auth.TokenStr
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, Controller, ControllerComponents}
import utils.Contexts

@Singleton
class TokenController @Inject()(contexts: Contexts, tokenService: TokenService, cc: ControllerComponents) extends AbstractController(cc) {
  implicit val executionContext = contexts.cpuLookup

  def refreshToken(token: String) = Action.async{
    tokenService.authenticateToken(TokenStr(token), true).map(x => Ok(Json.toJson(x)))
  }

  def authenticate(token:String) = Action.async{
    tokenService.authenticateToken(TokenStr(token), true).map(x => Ok(Json.toJson(x)))
  }
}
