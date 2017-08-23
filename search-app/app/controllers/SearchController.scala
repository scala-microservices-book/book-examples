package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class SearchController @Inject()(cc: ControllerComponents,
                            ws: WSClient)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {


  def get(name: String) = Action.async { implicit request =>
    //below will make a call to
    //http://httpbin.org/get?username=ronaldo
    ws.url("http://httpbin.org/get")
      .addHttpHeaders("Content-Type" -> "application/json")
      .withQueryStringParameters("userame" -> name)
      .get().map { response =>
      Ok(response.body)
    }
  }
}