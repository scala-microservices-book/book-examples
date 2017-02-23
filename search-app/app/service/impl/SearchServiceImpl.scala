package service.impl

import java.io.File

import service.SearchService

import scala.concurrent.{ExecutionContext, Future}


object SearchServiceImpl extends SearchService{
  override def search(word: String, file: File)(implicit exec: ExecutionContext): Future[Boolean] = Future{
    val content = scala.io.Source.fromFile(file)
    content.getLines().exists(_ contains word)
  }
}
