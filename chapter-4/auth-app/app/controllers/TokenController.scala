package controllers

import javax.inject.{Inject, Singleton}

import com.microservices.auth.TokenStr
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{TokenService, UserService}
import utils.Contexts

@Singleton
class TokenController @Inject()(contexts: Contexts, tokenService: TokenService) extends Controller {
  implicit val executionContext = contexts.cpuLookup

  def refreshToken(token: String) = Action.async{
    tokenService.refreshToken(TokenStr(token)).map(x => Ok(Json.toJson(x)))
  }
}
