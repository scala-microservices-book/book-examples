package controller

import javax.inject.{Inject, Singleton}

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class LoginController @Inject()() extends Controller {

  def status = Action { request =>
    request.session.get("email").map { email =>
      Ok(Json.parse("""{"message":"success"}"""))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }


  case class UserData(email: String, password: String)

  val userForm = Form(
    mapping(
      "email" -> text,
      "password" -> text
    )(UserData.apply)(UserData.unapply)
  )

  def login = Action(parse.form(userForm)) { implicit request =>
    val body = request.body
    Ok(Json.parse("""{"message":"success"}""")).withSession(
      "email" -> "test@test.com")
  }

  def logout = Action {
    // Delete session entry for request SID
    Ok(Json.parse("{\"message\":\"success\"}")).withNewSession
    //    Ok(Json.parse("{\"message\":\"success\"}"))
  }
}
