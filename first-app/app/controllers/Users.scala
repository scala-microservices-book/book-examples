package controllers

import play.api.mvc.{Action, Controller}


object Users extends Controller{

  def getAllUsers = Action{ implicit request =>
    Ok.apply("")
  }

  def getUser(name:String, age:Int) = Action{ implicit request =>
    Ok(s"Hello $name of age: $age")
  }

  def addUser() = Action{ implicit request =>
    val body = request.body

    body.asFormUrlEncoded match{
      case Some(map) =>
        //persist user information
        Ok(s"The user of name `${map("name").head}` and age `${map("age").head}` has been created\n")
      case None => BadRequest("Unknow body format")
    }
  }
}
