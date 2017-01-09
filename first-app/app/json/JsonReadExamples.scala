package json

import play.api.libs.functional.syntax._
import play.api.libs.json._


object JsonReadExamples {
  def main(args: Array[String]): Unit = {
    read2
    read3
  }

  case class Team(teamName: String, players: List[Player], location: Location)

  case class Player(name: String, age: Int)

  case class Location(lat: Double, long: Double)

  val jsonString =
    """{
      |  "teamName" : "Real Madrid FC",
      |  "players" : [ {
      |    "name" : "Ronaldo",
      |    "age" : 36
      |  }, {
      |    "name" : "Modric",
      |    "age" : 30
      |  }, {
      |    "name" : "Bale",
      |    "age" : 27
      |  } ],
      |  "location" : {
      |    "lat" : 40.4168,
      |    "long" : 3.7038
      |  }
      |}
      |""".stripMargin


  def read1() = {
    val jValue = Json.parse(jsonString)
    println((jValue \ "teamName").as[String])
    println((jValue \ "location" \ "lat").as[Double])
    println(((jValue \ "players") (0) \ "name").as[String])

    val validate: JsResult[String] = (jValue \ "teaName").validate[String]
    validate match {
      case x:JsSuccess[String] => println(x.get)
      case e: JsError => println(e.errors)
    }

    val names: Seq[JsValue] = jValue \\ "name"
    println(names.map(x => x.as[String]))
  }

  /**
    * In the below method, we manually generate Reads object for each case class type
    */
  def read2() = {

    val jValue: JsValue = Json.parse(jsonString)

    val temp: Reads[Double] = (JsPath \ "location" \ "lat").read[Double]

    println(jValue.as[Double](temp))


    implicit val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[Double] and
        (JsPath \ "long").read[Double]
      ) (Location.apply _)

    implicit val playerReads: Reads[Player] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "age").read[Int]
      ) (Player.apply _)

    implicit val teamReads: Reads[Team] = (
      (JsPath \ "teamName").read[String] and
        (JsPath \ "players").read[List[Player]] and
        (JsPath \ "location").read[Location]
      ) (Team.apply _)


    val teams = jValue.as[Team]

  }


  def read3 = {
    val jValue: JsValue = Json.parse(jsonString)

    implicit val playerReads = Json.reads[Player]
    implicit val locationReads = Json.reads[Location]
    implicit val teamReads = Json.reads[Team]

    val teams = Json.fromJson[Team](jValue).get


  }

}

