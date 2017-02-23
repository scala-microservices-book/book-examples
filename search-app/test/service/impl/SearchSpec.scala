package service.impl

import java.io._

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnitRunner])
object SearchSpec extends Specification {

  "SearchService" should {
    "search word in file" in {
      val file = prepareFile("hello.txt", "hi")

      val ans: Future[Boolean] = SearchServiceImpl.search("hi", file)

      Await.result(ans, 10 seconds) must equalTo(true)
    }

    "search in all nested files" in {
      val root = new File("root")
      root.mkdir()
      val hi0 = prepareFile("hello0.txt", "hi0", root)
      prepareFile("hello1.txt", "hi1", root)
      prepareFile("hello2.txt", "hi2", root)

      val folder = new File(root, "test")
      folder.mkdir()
      val hi1 = prepareFile("hello3.txt", "hi0",folder)
      val hi2 = prepareFile("hello4.txt", "hi0",folder)

      Await.result(SearchServiceImpl.searchInAll("hi0", root), 1000 seconds).toSet must equalTo(Set(hi0, hi1, hi2))

    }

  }

  private def prepareFile(fileName: String, wordToContain: String, parentFolder:File = new File(".")) = {
    val f = new File(parentFolder, fileName)
    f.deleteOnExit()

    val writer = new BufferedWriter(new FileWriter(f))
    for (i <- 1 to 10) {
      writer.write(Random.nextString(i))
      writer.write("\n")
    }
    writer.write(wordToContain)
    writer.close()
    f
  }

}

