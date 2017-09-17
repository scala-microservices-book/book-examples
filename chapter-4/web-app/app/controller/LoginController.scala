package controller

import javax.inject.{Inject, Singleton}

import com.microservices.auth._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsError, JsSuccess}
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.AllProperties

import scala.concurrent.ExecutionContext

@Singleton
class LoginController @Inject()(cc: ControllerComponents, config: AllProperties, ws: WSClient, security: SecurityAction)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def status = security { request => Ok(ResponseObj.asSuccess("success"))}


  case class UserData(email: String, password: String)

  val userForm = Form(
    mapping(
      "email" -> text,
      "password" -> text
    )(UserData.apply)(UserData.unapply)
  )

  def login = Action.async(parse.form(userForm)) { implicit request =>
    val body = request.body
    ws.url(config.authURL + "v1/auth/login")
      .addHttpHeaders("Accept" -> "application/json")
      .post(User.userJS.writes(User(body.email, body.password)))
      .map {
        response =>
          response.json.validate(ResponseObj.reads[TokenStr]) match {
            case s: JsSuccess[_] => s.get match {
              case SuccessRes(token: TokenStr) =>
                Ok(ResponseObj.asSuccess(token))
                  .withSession("token" -> token.tokenStr)
              case FailureRes(msg) => Unauthorized(ResponseObj.asFailure("authentication failure: "+msg))
              case x => InternalServerError(ResponseObj.asFailure("internal server error. "+x))
            }
            case e: JsError => Unauthorized(ResponseObj.asFailure("authentication failure"))
          }
      }
  }


  def register = Action.async(parse.form(userForm)) { implicit request =>
    val body = request.body
    ws.url(config.authURL + "v1/auth/register")
      .addHttpHeaders("Accept" -> "application/json")
      .post(User.userJS.writes(User(body.email, body.password)))
      .map {
        response =>
          response.json.validate(ResponseObj.reads[TokenStr]) match {
            case s: JsSuccess[_] => s.get match {
              case SuccessRes(token: TokenStr) =>
                Ok(ResponseObj.asSuccess(token))
                  .withSession("token" -> token.tokenStr)
              case FailureRes(msg) => Unauthorized(ResponseObj.asFailure("could not register user: "+msg))
              case x => InternalServerError(ResponseObj.asFailure("internal server error. "+x))
            }
            case e: JsError => Unauthorized(ResponseObj.asFailure("could not register user"))
          }
      }
  }


  def logout = Action {
    // Delete session entry for request SID
    Ok(ResponseObj.asSuccess("success")).withNewSession
  }
}
