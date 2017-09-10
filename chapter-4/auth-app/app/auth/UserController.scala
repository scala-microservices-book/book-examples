package auth

import javax.inject.{Inject, Singleton}

import com.microservices.auth.{TokenStr, User}
import play.api.libs.json.Json
import play.api.mvc._
import tokens.TokenService
import utils.Contexts

import scala.concurrent.Future

@Singleton
class UserController @Inject()(userService: UserService, contexts: Contexts, tokenService: TokenService, cc: ControllerComponents) extends AbstractController(cc) {

  implicit val executionContext = contexts.cpuLookup

  /**
    * Function to register a new user created.
    *
    * Expects a json corresponding to the `com.microservices.auth.User` object in the request body. It creates a token and responds
    * the token to the caller.
    */
  /*
  Example call:
  curl -X POST \
    http://localhost:5001/v1/auth/register \
    -H 'content-type: application/json' \
    -d '{
	    "email":"p@p.com",
	    "password":"abcd"
    }'
   */
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
          else Future.successful(BadRequest("username/password mismatch"))
        }
    )
  }

  def logout(token: String) = Action.async { implicit request =>
    val future = tokenService.authenticateToken(TokenStr(token), refresh = false)
    future.map(x => {
      tokenService.dropToken(x.token)
      Ok("loggedout") //TODO
    }).recoverWith {
      case e: Exception => Future.successful(BadRequest(e.getMessage))
    }
  }


  def getAll = Action.async {
    userService.getAllUserNames.map(x => Ok(Json.toJson(x)))
  }

}
