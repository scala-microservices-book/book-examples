package controller

import javax.inject.Inject

import com.microservices.auth.{ResponseObj, User}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.ws.WSClient
import play.api.mvc._
import utils.AllProperties

import scala.concurrent.{ExecutionContext, Future}

//class SecurityAction @Inject()(parser: BodyParsers.Default, config: AllProperties, ws: WSClient)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
//
//  override def invokeBlock[A](implicit request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
//    Logger.info("Calling action")
//    userForm.bindFromRequest().value match {
//      case Some(x) =>
//        ws.url(config.stackoverflowURL + "v1/auth/login")
//          .post(User(x.email, x.password))
//          .map(x => block(request))
//          .recoverWith {
//            case e: Exception => Future.successful(Results.BadRequest(ResponseObj.asFailure(e.getMessage)))
//          }
//
//      case None =>
//        Future.successful(Results.BadRequest(ResponseObj.asFailure(s"authentication failure")))
//    }
//
//    block(request)
//  }
//
//  case class UserData(email: String, password: String)
//
//  val userForm = Form(
//    mapping(
//      "email" -> text,
//      "password" -> text
//    )(UserData.apply)(UserData.unapply)
//  )
//
//}