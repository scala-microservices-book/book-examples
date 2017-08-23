package service

import java.io.File

import scala.concurrent.{ExecutionContext, Future}

trait SearchService {

  def search(word: String, file: File)(implicit exec: ExecutionContext): Future[Boolean]

  def searchInAll(word: String, root: File)(implicit exec: ExecutionContext): Future[Seq[File]] = {
    if (!root.isDirectory) {
      search(word, root).map(found => if (found) Seq(root) else Seq())
    } else {
      val all = root.listFiles().toList.map(x => searchInAll(word, x))
      Future.sequence(all).map(x => x.flatten)
    }
  }

}
