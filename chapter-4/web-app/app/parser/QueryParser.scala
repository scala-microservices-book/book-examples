package parser

import scala.util.parsing.combinator.RegexParsers


object QueryParser {

  /*
    Scala in london
  Java developers in new york
  Javascript developers in San Jose
   */

  def parse(query: String): Either[String, SearchQuery]= {
    SearchParser(query)
  }


  object SearchParser extends RegexParsers{

    private def tag = "[^\\s]+".r ^^ (x => Tag(x))


    private def in = """(?i)\Qin\E""".r

    private def developer = """(?i)\Qdeveloper\E""".r

    private def city = ".+".r ^^ (name => City(name))

    private def expr = (((tag <~ opt(developer)) <~ in) ~ city) ^^ (x => SearchQuery(x._1, x._2))

    def apply(st: String): Either[String, SearchQuery] = parseAll(expr, st) match {
      case Success(ob, _) => Right(ob)
      case NoSuccess(msg, _) => Left(msg)
    }
  }


  sealed abstract class Element

  case class Tag(name:String) extends Element
  case class City(name:String) extends Element
  case class SearchQuery(tags: Tag, city: City) extends Element
}
