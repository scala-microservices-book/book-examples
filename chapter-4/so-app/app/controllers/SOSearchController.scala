package controllers

import javax.inject.{Inject, Singleton}

import com.microservices.search.{SOSearchResult, SearchFilter}
import play.api.libs.json.Json
import play.api.mvc._
import service.SearchService
import users.Contexts

import scala.collection.immutable.Seq
import scala.concurrent.Future

@Singleton
class SOSearchController @Inject()(service: SearchService, context: Contexts, cc: ControllerComponents) extends AbstractController(cc) {

  import context.cpuLookup

  def searchPost = Action.async(parse.json) { implicit request =>
    val body = request.body
    val loc = (body \ "location").validate[String]
    val tag = (body \ "tag").validate[String]
    if (loc.isError || tag.isError) {
      Future.successful(BadRequest(s"Not a valid input: $body"))
    } else {
      val filter = SearchFilter(Option(loc.get), Option(tag.get))
      search(filter).map(x => Ok(Json.toJson(x)))
    }
  }

  //  def searchPost = Action.async(parse.json) { implicit request =>
  //    val body = request.body
  //    val ans = body.validate[SearchFilter] match {
  //      case s: JsSuccess[SearchFilter] =>
  //        val filter: SearchFilter = s.get
  //        search(SearchFilter(filter.city, filter.tags)).map(x => Ok(ResponseObj.asSuccess(x)))
  //      case s: JsError => Future.successful(BadRequest(s"Not a valid input: $body"))
  //    }
  //    ans
  //  }

  def searchGet(location: String, tag: String) = Action.async { implicit request =>
    search(SearchFilter(Option(location), Option(tag))).map(x => Ok(Json.toJson(x)))
  }

  private def search(filter: SearchFilter): Future[Seq[SOSearchResult]] = {
    service.searchFlatten(filter)
  }

}
