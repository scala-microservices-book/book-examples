package controllers

import play.api.mvc.{Action, Controller}


object LoginController extends Controller {
  def login(name: String, password: String) = Action { implicit request =>
    if (check(name, password))
      Redirect(routes.LoginController.index())
        .withSession(("user", name))
    else BadRequest("Invalid password")
  }

  def index = Action{ implicit request =>
    request.session.get("user") match {
      case Some(user) if isValidUser(user)=> Ok(s"Welcome $user")
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("Please login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def logout = Action{ implicit  request =>
    request.session.get("user") match {
      case Some(user) if isValidUser(user)=> Ok("Successfully logged out").withNewSession
      case Some(user) => BadRequest("Not a valid user")
      case None => BadRequest("Not logged in. Please login by calling: \n" +
        "http://localhost:9000/auth/login?name=admin&password=1234")
    }
  }

  def check(name: String, password: String) = {
    name == "admin" && password == "1234"
  }

  def isValidUser(name:String) ={
    name == "admin"
  }

}
