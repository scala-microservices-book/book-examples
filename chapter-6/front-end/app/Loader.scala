import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.softwaremill.macwire._
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.ApplicationLoader.Context
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class FrontEndModule(context: Context) extends BuiltInComponentsFromContext(context)
  with I18nComponents
  with AhcWSComponents
  with LagomServiceClientComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "front-end",
    Map(
      "front-end" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api_jatin/).*"))
    )
  )

  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }

}

class FrontEndLoader extends ApplicationLoader {
  override def load(context: Context) = context.environment.mode match {
    case Mode.Dev =>
      (new FrontEndModule(context) with LagomDevModeComponents).application
    case _ =>
      (new FrontEndModule(context) with LagomDevModeComponents).application
  }
}