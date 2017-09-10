package parser

import com.microservices.search.SearchQuery

import scala.util.parsing.combinator.RegexParsers


object QueryParser {

  /*
    Scala in london
  Java developers in new york
  Javascript developers in San Jose
   */

  /**
    * Returns a either whose left would be failure cause. and right would be the result
    *
    * @param query
    * @return
    */
  def parse(query: String): Either[String, SearchQuery] = {
    SearchParser(query)
  }


  object SearchParser extends RegexParsers {

    private def tag = "[^\\s]+".r ^^ (x => x)

    //    private def in = """(?i)\Qin\E""".r
    private def in =
      """(?i)i(?i)n""".r

    //    private def developer = """(?i)\Qdevelopers?\E""".r
    //(?i) is to ignore the case of first character (d or D)
    private def developer =
    """(?i)d(?i)e(?i)v(?i)e(?i)l(?i)o(?i)p(?i)e(?i)r(?i)s?""".r

    private def city = ".+".r ^^ (name => name)

    private def expr = (((opt(tag) <~ opt(developer)) <~ opt(in)) ~ opt(city)) ^^ (x =>
      SearchQuery(x._1.filter(x => !x.toLowerCase().startsWith("developer")), x._2))

    def apply(st: String): Either[String, SearchQuery] = parseAll(expr, st) match {
      case Success(ob, _) => Right(ob)
      case NoSuccess(msg, _) => Left(msg)
    }
  }

}
