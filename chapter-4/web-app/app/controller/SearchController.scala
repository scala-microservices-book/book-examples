package controller

import javax.inject.{Inject, Singleton}

import com.microservices.auth.ResponseObj
import com.microservices.search.SearchFilter
import parser.QueryParser
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.AllProperties

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SearchController @Inject()(securityAction: SecurityAction, cc: ControllerComponents, config: AllProperties, ws: WSClient)(implicit val exec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * The search syntax will be of the form:
    *
    * `(scala, abcd) opt(developers) in (city)`
    *
    * @return
    */
  def search(location: Option[String], tag: Option[String]) = securityAction.async { implicit request =>

    /**
      * Get DNS for so-app service. This can come from a file.properties. k8s can load the file via a configMap.
      */
    getResultsForQuery(SearchFilter(location, tag)).map(Ok(_))
  }


  def searchQuery(query: String) = Action.async { implicit request =>
    QueryParser.parse(query) match {
      case Left(err) =>
        Future(Ok(ResponseObj.asFailure("could not parse query. error: " + query)))
      case Right(searchFilter) => getResultsForQuery(searchFilter)
          .map(x => Ok(x))
        .recoverWith {
        case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure(e.getMessage)))
      }
    }
  }

  private def getResultsForQuery(q: SearchFilter): Future[String] = {
      ws.url(config.rankURL + "api/v1/search")
        .addHttpHeaders("Accept" -> "application/json")
        .post(SearchFilter.format.writes(q))
        .map(x => {
          x.body
        })
    }
}