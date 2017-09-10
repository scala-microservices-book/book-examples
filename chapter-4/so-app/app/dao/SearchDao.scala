package dao

import javax.inject.Singleton

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.GetResult
import users.{SOTag, SOUser, SoUserScore}

import scala.collection.immutable.Iterable
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class SearchDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  implicit val getUserResult: GetResult[(SOUser, SOTag, Int)] = GetResult(r =>
    (SOUser(r.nextInt(), r.nextString(), r.nextInt(), r.nextString(), r.nextString(), r.nextString()),
      SOTag(r.nextInt(), r.nextString),
      r.nextInt()))


  def getUsers(location: Option[String], tag: Option[String])(implicit exec:ExecutionContext): Future[Iterable[SoUserScore]] = {

    val selectQ =
      """select a.id,a.name, a.so_account_id, a.about_me, a.so_link, a.location, c.id,c.name,b.points from so_user_info a
            join so_reputation b
              on b.user=a.id
            join so_tag c on b.tag=c.id where 1=1 limit 10"""

    val allFuture = (location, tag) match {
      case (Some(loc), Some(t)) =>
        db.run(sql"""#$selectQ""".as[(SOUser, SOTag, Int)])
        // db.run(sql"""#$selectQ
        //       AND UPPER (a.location) = UPPER(${loc})
        //       AND UPPER (c.name) = UPPER (${t})""".as[(SOUser, SOTag, Int)])
      case (Some(loc), None) =>
        db.run(sql"""#$selectQ""".as[(SOUser, SOTag, Int)])
        // db.run(sql"""#$selectQ
        //       AND UPPER (a.location) = UPPER(${loc})""".as[(SOUser, SOTag, Int)])
      case (None, Some(t)) =>
        db.run(sql"""#$selectQ""".as[(SOUser, SOTag, Int)])
        // db.run(sql"""#$selectQ
        //       AND UPPER (c.name) = UPPER (${t})""".as[(SOUser, SOTag, Int)])
      case (None, None) => db.run(sql"""#$selectQ""".as[(SOUser, SOTag, Int)])
    }


    allFuture.map(allUsers => {
      allUsers.groupBy(x => x._1).map(pair => {
        SoUserScore(pair._1, pair._2.map(x => (x._2, x._3)).toMap)
      })
    })

  }
}
