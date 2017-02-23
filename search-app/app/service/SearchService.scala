package service

import java.io.File

import scala.concurrent.{ExecutionContext, Future}

trait SearchService {

  def search(word: String, file: File)(implicit exec: ExecutionContext): Future[Boolean]

  def searchInAll(word: String, root: File)(implicit exec: ExecutionContext): Future[Seq[File]] = {
    if (!root.isDirectory) {
      throw new IllegalArgumentException("input must be a directory")
    }

    val all = root.listFiles().toSeq.map(file => {
      if (file.isDirectory)
        searchInAll(word, file)
      else search(word, file).map(exists => if(exists) Seq(file) else Seq())
    })

    Future.sequence(all).map(x => x.flatten)
  }

}
