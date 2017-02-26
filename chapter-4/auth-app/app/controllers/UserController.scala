package controllers

import javax.inject.{Inject, Singleton}

import com.microservices.auth.User
import play.api.libs.json.Json
import play.api.mvc._
import services.{TokenService, UserService}
import utils.Contexts

import scala.concurrent.Future

@Singleton
class UserController @Inject()(userService: UserService, contexts: Contexts, tokenService: TokenService) extends Controller {

  implicit val executionContext = contexts.cpuLookup

  def register = Action.async(parse.json) { implicit request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest("Not a valid input format: " + error.mkString)),
      user =>
        userService.userExists(user.email).flatMap(ifExists => {
          if (ifExists)
            Future.successful(BadRequest(s"User already exists: ${user.email}. cannot register again"))
          else {
            userService.addUser(user)
              .flatMap(_ => tokenService.createToken(user.email))
              .map(x => Ok(Json.toJson(x.token)))
          }
        })
    )
  }

  def login = Action.async(parse.json) { implicit request =>
    request.body.validate[User].fold(
      error => Future.successful(BadRequest("Not a valid input format: " + error.mkString)),
      user =>
        userService.validateUser(user.email, user.password).flatMap { validated =>
          if (validated) tokenService.createToken(user.email).map(x => Ok(Json.toJson(x.token)))
          else Future.successful(BadRequest("user does not exist"))
        }
    )
  }

  def getAll = Action.async {
    userService.getAllUserNames.map(x => Ok(Json.toJson(x)))
  }

}
