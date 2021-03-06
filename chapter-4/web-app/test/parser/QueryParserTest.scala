package parser

import com.microservices.search.SearchFilter
import org.scalatest.{FlatSpec, Matchers}


class QueryParserTest  extends FlatSpec with Matchers{


  "Query Parser" should "Scala in london" in {
    val ans = QueryParser.parse("Scala in london")
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("london"), Some("Scala")))
  }

  "Query Parser" should "Java developers in new york" in {
    val ans = QueryParser.parse("Java developers in new york")
    println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("new york"), Some("Java")))
  }

  "Query Parser" should "Java DEVELOPERS iN new york" in {
    val ans = QueryParser.parse("Java developers in new york")
    println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("new york"), Some("Java")))
  }


  "Query Parser" should "Java developer in new york" in {
    val ans = QueryParser.parse("Java developer in new york")
    println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("new york"), Some("Java")))
  }

  "Query Parser" should "Java Developer in new york" in {
    val ans = QueryParser.parse("Java Developer in new york")
    println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("new york"), Some("Java")))
  }


  "Query Parser" should "Scala developers" in {
    val ans = QueryParser.parse("Scala developers")
    System.err.println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(None, Some("Scala")))
  }

  "Query Parser" should "Scala Developers" in {
    val ans = QueryParser.parse("Scala Developers")
    System.err.println(ans)
    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(None, Some("Scala")))
  }

  "Query Parser" should "developers in san jose" in {
    val ans = QueryParser.parse("developers in san jose")
    println(ans)

    ans.isLeft should be (false)
    ans.right.get should be (SearchFilter(Some("san jose"), None))
  }
//  "Query Parser" should "San Francisco" in {
//    val ans = QueryParser.parse("San Francisco")
//    ans.isLeft should be (false)
//    ans.right.get should be (SearchFilter(None, Some("San Francisco")))
//  }


}
