package json

import play.api.libs.functional.syntax._
import play.api.libs.json._


object JsonWriteExamples {
  def main(args: Array[String]): Unit = {
    write1
    write4

  }


  val team: Team = Team("Real Madrid FC", List(
    Player("Ronaldo", 31),
    Player("Modric", 30),
    Player("Bale", 27)
  ), Location(40.4168, 3.7038))


  private def write1 = {
    val json: JsValue = JsObject(Seq(
      "teamName" -> JsString("Real Madrid FC"),
      "players" -> JsArray(Seq(
        JsObject(Seq(
          "name" -> JsString("Ronaldo"),
          "age" -> JsNumber(32))),
        JsObject(Seq(
          "name" -> JsString("Modric"),
          "age" -> JsNumber(29))),
        JsObject(Seq(
          "name" -> JsString("Bale"),
          "age" -> JsNumber(28)))
      )),
      "location" -> JsObject(Seq(
        "lat" -> JsNumber(40.4168),
        "long" -> JsNumber(3.7038)))
    ))
    println(json.toString())
  }

  private def write2 = {
    val json: JsValue = Json.obj(
      "teamName" -> "Real Madrid FC",
      "players" -> Json.arr(
        Json.obj(
          "name" -> JsString("Ronaldo"),
          "age" -> JsNumber(32)),
        Json.obj(
          "name" -> "Modric",
          "age" -> 29),
        Json.obj(
          "name" -> "Bale",
          "age" -> 28)
      ),
      "location" -> Json.obj(
        "lat" -> 40.4168,
        "long" -> 3.7038)
    )
    println(json.toString())
  }

  private def write3 = {

    Json.obj(
      "teamName" ->  team.teamName,
      "players" -> Json.arr(
        team.players.map(x => Json.obj(
          "name" -> x.name,
          "age" -> x.age
        ))),
      "location" -> Json.obj(
        "lat" -> team.location.lat,
        "lat" -> team.location.long
      )
    )
  }


  private def write4 = {
    implicit val locationWrites: Writes[Location] = (
      (JsPath \ "lat").write[Double] and
        (JsPath \ "long").write[Double]
      )(unlift(Location.unapply))

    implicit val playerWrites: Writes[Player] = (
      (JsPath \ "name").write[String] and
        (JsPath \ "age").write[Int]
      )(unlift(Player.unapply))

    implicit val teamWrites: Writes[Team] = (
      (JsPath \ "teamName").write[String] and
        (JsPath \ "players").write[List[Player]] and
        (JsPath \ "location").write[Location]
      )(unlift(Team.unapply))

    println(Json.toJson(team).toString())

  }

  def writes5={
    implicit val residentWrites = Json.writes[Player]
    implicit val locationWrites = Json.writes[Location]
    implicit val positionWrites = Json.writes[Team]


    println(Json.prettyPrint(Json.toJson(team)))

  }

  case class Team(teamName: String, players: List[Player], location: Location)

  case class Player(name: String, age: Int)

  case class Location(lat: Double, long: Double)


}




