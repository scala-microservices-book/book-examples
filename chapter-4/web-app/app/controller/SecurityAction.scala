package controller

import javax.inject.Inject

import com.microservices.auth.ResponseObj
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.AllProperties

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class does authentication based on the session.
  */
class SecurityAction @Inject()(parser: BodyParsers.Default, config: AllProperties, ws: WSClient)
                              (implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Calling action")
    request.session.get("token") match {
      case Some(token) =>
        ws.url(config.authURL + "v1/tokens/authenticate/" + token)
          .get()
          .flatMap(x => {
            Logger.info("Auth successful.. " + x)
            block(request)
          })
          .recoverWith {
            case e: Exception =>
              Future.successful(Results.BadRequest(ResponseObj.asFailure(e.getMessage)))
          }
      case None =>
        Logger.info("Not logged in. Please login")
        Future.successful(Results.BadRequest(ResponseObj.asFailure(s"Not logged in. Please login")))
//        block(request)
    }
  }
}