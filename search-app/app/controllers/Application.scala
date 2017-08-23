package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.mvc._

@Singleton
class Application @Inject() (cc: ControllerComponents, akkaSystem: ActorSystem) extends AbstractController(cc) {

  implicit val exec = akkaSystem.dispatchers.lookup("contexts.db-lookups")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}

