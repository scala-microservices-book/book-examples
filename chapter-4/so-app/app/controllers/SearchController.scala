package controllers

import javax.inject.{Inject, Singleton}

import com.microservices.search.SearchFilter
import play.api.libs.json.Json
import play.api.mvc._
import service.SearchService
import users.{Contexts, SOSearchResult}

import scala.collection.immutable.Seq
import scala.concurrent.Future

@Singleton
class SearchController @Inject()(service: SearchService, context:Contexts, cc: ControllerComponents) extends AbstractController(cc) {

  import context.cpuLookup

  def searchPost = Action.async(parse.json) { implicit request =>
    val body = request.body
    val loc = (body \ "location").validate[String]
    val tag = (body \ "tag").validate[String]
    val test = (body \ "test").validate[Boolean]

    if(loc.isError || tag.isError){
      Future.successful(BadRequest(s"Not a valid input: $body"))
    }else if(test.isSuccess && test.get){
      val response = """[{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":4,"name":"Sophia","soAccountId":4,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":2,"name":"Muhammad","soAccountId":2,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":22,"name":"Jackson","soAccountId":22,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"singapore"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":15,"name":"Anna","soAccountId":15,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":19,"name":"Mackenzie Julian","soAccountId":19,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":1,"name":"Madelyn Carter","soAccountId":1,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san francisco"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":33,"name":"Lily","soAccountId":33,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":20,"name":"Addison Sebastian","soAccountId":20,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san francisco"}}]"""
      Future(Ok(Json.toJson(response)))
    }
    else{
      val filter = SearchFilter(Option(loc.get),Option(tag.get))
      search(filter).map(x => Ok(Json.toJson(x)))
    }
  }

  def searchGet(location:String, tag:String) =  Action.async { implicit request =>
      search(SearchFilter(Option(location), Option(tag))).map(x => Ok(Json.toJson(x)))
  }

  private def search(filter:SearchFilter): Future[Seq[SOSearchResult]] = {
    service.searchFlatten(filter)
  }

}
