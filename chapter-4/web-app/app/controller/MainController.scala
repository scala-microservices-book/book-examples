package controller

import javax.inject.{Inject, Singleton}

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class MainController @Inject()() extends Controller{

  def status= Action{
    // Check for valid session here.
    // return 403 if session not found
    Ok(Json.parse("{\"message\":\"success\"}"))
  }


  case class UserData(email:String, password: String)

  val userForm = Form(
    mapping(
      "email" -> text,
      "password" -> text
    )(UserData.apply)(UserData.unapply)
  )

  def login = Action(parse.form(userForm)){implicit request =>
    val body = request.body

    Ok(
      """
        |{
        | "message" : "succes"
        |}
      """.stripMargin)
  }

  def logout = Action{
    // Delete session entry for request SID
    Ok(Json.parse("{\"message\":\"success\"}"))
  }
}
