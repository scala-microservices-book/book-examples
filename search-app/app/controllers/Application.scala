package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.ws.WSClient
import play.api.mvc._


@Singleton
class Application @Inject() (ws: WSClient, akkaSysten: ActorSystem) extends Controller {

  import play.api.libs.concurrent.Execution.Implicits._

  def index = Action {
      Ok(views.html.index("Your new application is ready."))
  }

  def search(name:String) = Action.async { implicit request =>
    ws.url("http://www.google.com").get().map { response =>
      Ok(response.body)
    }
  }

}
