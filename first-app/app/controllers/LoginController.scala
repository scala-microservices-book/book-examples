package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc._

@Singleton
class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def login(name: String, password: String) = Action {
    if (check(name, password))
      Redirect("/auth/index")
        .withSession(("user", name))
    else BadRequest("Invalid username or password")
  }

  def index = Action { implicit request =>
    request.session.get("user") match {
      case Some(user) if isValidUser(user) => Ok(s"Welcome $user")
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("You are currently not logged in. \nPlease login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def logout = Action { implicit request =>
    request.session.get("user") match {
      case Some(user) if isValidUser(user) => Ok("Successfully logged out").withNewSession
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("Not logged in. Please login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def check(name: String, password: String): Boolean = {
    name == "admin" && password == "1234"
  }

  def isValidUser(name: String): Boolean = {
    name == "admin"
  }

}
