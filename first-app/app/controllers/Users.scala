package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc._

@Singleton
class Users @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getAllUsers = Action {
    Ok.apply("")
  }

  def getUser(name: String, age: Int) = Action {
    Ok(s"Hello $name of age: $age")
  }

  def addUser() = Action { implicit request =>
    val body = request.body

    body.asFormUrlEncoded match {
      case Some(map) =>
        //persist user information
        Ok(s"The user of name `${map("name").head}` and age `${map("age").head}` has been created\n")
      case None => BadRequest("Unknow body format")
    }
  }
}
