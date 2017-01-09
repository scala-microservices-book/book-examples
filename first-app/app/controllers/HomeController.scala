package controllers

import play.api.mvc._
import play.twirl.api.Html

import scala.util.{Failure, Success, Try}

object HomeController extends Controller{

  def index() = Action{implicit request =>
    Ok("Hello World!")
      .withHeaders("Server" -> "Play")
      .withCookies(Cookie("id", scala.util.Random.nextInt().toString))
  }

  def sqrt(num:String) = Action{ implicit request =>
    Try(num.toInt) match {
      case Success(ans) if ans >= 0 => Ok(s"The answer is: ${math.sqrt(ans)}")
      case Success(ans) => BadRequest(s"The input ($num) must be greater than zero")
      case Failure(ex) => InternalServerError(s"Could not extract the contents from $num")
    }
  }


  def hello(name:String) = Action{implicit request =>
    val c: Html = views.html.index(name)
    Ok(c)
//    Ok("Hello "+name)
  }



}
