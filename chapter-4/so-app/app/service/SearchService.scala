package service

import javax.inject.{Inject, Singleton}

import com.microservices.search.SearchFilter
import dao.SearchDao
import play.api.Logger
import users.{SOSearchResult, SoUserScore}

import scala.collection.immutable.{Iterable, Seq}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SearchService @Inject()(dao: SearchDao) {

  private val log = Logger(getClass)

  def search(filter: SearchFilter)(implicit exec: ExecutionContext): Future[Iterable[SoUserScore]] = {
    dao.getUsers(filter.location, filter.tech)
  }

  def searchFlatten(filter: SearchFilter)(implicit exec: ExecutionContext): Future[Seq[SOSearchResult]] = {
    log.debug(s"Request for filter: $filter")

    search(filter).map(ans => {
      ans.toList.flatMap(x => x.map.map(tags => SOSearchResult(1, tags._1, x.user)))
    })
  }
}
