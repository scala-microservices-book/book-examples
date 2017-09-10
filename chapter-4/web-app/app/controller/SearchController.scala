package controller

import javax.inject.{Inject, Singleton}

import com.microservices.auth.{ResponseObj, User}
import com.microservices.search.SearchQuery
import parser.QueryParser
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.AllProperties

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SearchController @Inject()(cc: ControllerComponents, config: AllProperties, ws: WSClient)(implicit val exec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * The search syntax will be of the form:
    *
    * `(scala, abcd) opt(developers) in (city)`
    *
    * @return
    */
  def search(location: Option[String], tag: Option[String]) = Action { implicit request =>

    /**
      * Get DNS for so-app service. This can come from a file.properties. k8s can load the file via a configMap.
      */
    val response: String = """[{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":4,"name":"Sophia","soAccountId":4,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":2,"name":"Muhammad","soAccountId":2,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":22,"name":"Jackson","soAccountId":22,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"singapore"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":15,"name":"Anna","soAccountId":15,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":19,"name":"Mackenzie Julian","soAccountId":19,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":1,"name":"Madelyn Carter","soAccountId":1,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san francisco"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":33,"name":"Lily","soAccountId":33,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":20,"name":"Addison Sebastian","soAccountId":20,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san francisco"}}]"""
    Ok(Json.parse(response))
  }


  def searchQuery(query: String) = Action.async { implicit request =>
    QueryParser.parse(query) match {
      case Left(err) =>
        Future(Ok(ResponseObj.asFailure("could not parse query. error: " + query)))
      case Right(searchQuery) =>
        ws.url(config.stackoverflowURL + "so/v1/search")
          .addHttpHeaders("Accept" -> "application/json")
          .post(SearchQuery.format.writes(searchQuery))
          .map(x => {
            Ok(Json.toJson(x.body))
          }).recoverWith {
          case e: Exception => Future.successful(BadRequest(ResponseObj.asFailure(e.getMessage)))
        }

    }
  }
}
