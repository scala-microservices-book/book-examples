package controller

import javax.inject.{Inject, Singleton}

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
  def search(query:String) = Action { implicit request =>
    Ok("")
  }

}
