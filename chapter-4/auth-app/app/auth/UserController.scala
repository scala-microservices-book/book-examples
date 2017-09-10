package auth

import javax.inject.{Inject, Singleton}

import com.microservices.auth._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import tokens.TokenService
import utils.Contexts

import scala.concurrent.Future

 /**
  * The usernames along with hash of the passwords are stored in h2 database
  */
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
      error => Future.successful(BadRequest(ResponseObj.asFailure("Not a valid input format: " + error.mkString))),
      user =>
        userService.userExists(user.email).flatMap(ifExists => {
          if (ifExists)
            Future.successful(BadRequest(ResponseObj.asFailure(s"User already exists: ${user.email}. cannot register again")))
          else {
            userService.addUser(user)
              .flatMap(_ => tokenService.createToken(user.email))
              .map(x => Ok(ResponseObj.asSuccess(x.token)))
          }
        })
    )
  }

  /**
    * Verifies a user based on username and password. If valid user then returns the token belonging to that user
    */
  /*
   curl -X POST \
    http://localhost:5001/v1/auth/login \
    -H 'content-type: application/json' \
    -d '{
    "email":"p@p.com",
    "password":"abcd"
    }'
   */
  def login = Action.async(parse.json) { implicit request =>
    Logger.info("request received: "+request.body)
    request.body.validate[User].fold(
      error => Future.successful(BadRequest(ResponseObj.asFailure("Not a valid input format: " + error.mkString))),
      user =>
        userService.validateUser(user.email, user.password).flatMap { validated =>
          if (validated)
            tokenService.createToken(user.email).map(x => Ok(ResponseObj.asSuccess(x.token)))
          else Future.successful(BadRequest(ResponseObj.asFailure("username/password mismatch")))
        }
    )
  }

   /**
    * logsout the user be deleting the token associated with the user
    */
  def logout(token: String) = Action.async { implicit request =>
    val future = tokenService.authenticateToken(TokenStr(token), refresh = false)
    future.map(x => {
      tokenService.dropToken(x.token)
      Ok(ResponseObj.asFailure("loggedout"))
    }).recoverWith {
      case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure(e.getMessage)))
    }
  }


  def getAll = Action.async {
    userService.getAllUserNames.map(x => Ok(Json.toJson(x)))
  }

}
