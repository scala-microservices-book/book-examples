package controller

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}



@Singleton
class SearchController @Inject()() extends Controller{

  /**
    * The search syntax will be of the form:
    *
    * `(scala, abcd) opt(developers) in (city)`
    *
    *
    *
    * @param query
    * @return
    */
  def search(location:Option[String], tag:Option[String]) = Action { implicit request =>
    System.out.println(location)
    System.out.println(tag)
    /**
    *  @todo: Get DNS for so-app service. This can come from a file.properties. k8s can load the file via a configMap.
    */
    val response :String = """[{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":4,"name":"Sophia","soAccountId":4,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":2,"name":"Muhammad","soAccountId":2,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":22,"name":"Jackson","soAccountId":22,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"singapore"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":15,"name":"Anna","soAccountId":15,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":19,"name":"Mackenzie Julian","soAccountId":19,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"london"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":1,"name":"Madelyn Carter","soAccountId":1,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san francisco"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":33,"name":"Lily","soAccountId":33,"aboutMe":"10 PRINT I am the best. 20 GOTO 10","soLink":"#","location":"san jose"}},{"score":1,"soTag":{"id":1,"name":"scala"},"soUser":{"id":20,"name":"Addison Sebastian","soAccountId":20,"aboutMe":"Toy apps or cute things like qsort in haskell really give the wrong idea.","soLink":"#","location":"san francisco"}}]"""
    Ok(Json.parse(response))
  }
}
