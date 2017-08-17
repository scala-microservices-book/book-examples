package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.mvc._
import play.twirl.api.Html

import scala.util.{Failure, Success, Try}

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

    def index() = Action {
      Ok("Hello World!")
        .withHeaders("Server" -> "Play")
        .withCookies(Cookie("id", scala.util.Random.nextInt().toString))
    }

    def sqrt(num: String) = Action {
      Try(num.toInt) match {
        case Success(ans) if ans >= 0 => Ok(s"The answer is: ${math.sqrt(ans)}")
        case Success(ans) => BadRequest(s"The input ($num) must be greater than zero")
        case Failure(ex) => InternalServerError(s"Could not extract the contents from $num")
      }
    }


    def hello(name: String) = Action {
      val c: Html = views.html.index(name)
      Ok(c)
      //    Ok("Hello "+name)
    }

  }
