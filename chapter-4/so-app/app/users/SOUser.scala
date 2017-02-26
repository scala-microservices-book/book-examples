package users

import com.microservices.search.{SUserResult}
import play.api.libs.json.Json

case class SOUser(id:Long, name:String, soAccountId: Long, aboutMe:String, soLink:String="#", location:String)
object SOUser{
  implicit val soUserJSON = Json.format[SOUser]
}

case class SOTag(id:Int, name:String)

object SOTag{
  implicit val soTagJSON = Json.format[SOTag]
}


case class SoUserScore(user:SOUser, map: Map[SOTag, Int])

case class SOSearchResult(override val score:Float, soTag: SOTag, soUser: SOUser) extends SUserResult{
  override val location = soUser.location
  override val tag = soTag.name
}

object SOSearchResult{
  implicit val soSearchResultJSON = Json.format[SOSearchResult]
}
