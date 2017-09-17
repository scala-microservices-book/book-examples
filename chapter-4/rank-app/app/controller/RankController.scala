package controller

import javax.inject.Inject

import com.google.common.base.Throwables
import com.microservices.auth.{FailureRes, ResponseObj, SuccessRes}
import com.microservices.search.{SOSearchResult, SearchFilter}
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.RankProperties

import scala.concurrent.{ExecutionContext, Future}

class RankController @Inject()(cc: ControllerComponents, urls: RankProperties, ws: WSClient)(implicit val exec: ExecutionContext)
  extends AbstractController(cc) {

  def search(location: Option[String], tag: Option[String]) = Action.async { implicit request =>
    getResultsForQuery(SearchFilter(location, tag)).map(x => Ok(ResponseObj.asSuccess(x))).recoverWith {
      case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure(e.getMessage)))
    }
  }

  def searchByQuery = Action.async(parse.json) { implicit request =>
    val ans = getResultsForQuery(SearchFilter.format.reads(request.body).get).map(x => Ok(ResponseObj.asSuccess(x)))
    ans.recoverWith {
      case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure("stackoverflow call failed: "+Throwables.getStackTraceAsString(e))))
    }
  }

  private def getResultsForQuery(q: SearchFilter): Future[Seq[SOSearchResult]] = {
    ws.url(urls.stackoverflowURL+"so/v1/search")
      .addHttpHeaders("Accept" -> "application/json")
      .post(SearchFilter.format.writes(q))
      .map(x => {
        Logger.info(x.body)
        x.json.as[Seq[SOSearchResult]] match {
          case ans:Seq[_] => ans.asInstanceOf[Seq[SOSearchResult]]
          case e =>
            Logger.info("Unknown format response from stackoverflow "+e)
            throw new RuntimeException("Unknown format response from stackoverflow "+e)
        }
      })
  }
}
