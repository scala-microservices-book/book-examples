package auth

import javax.inject.Singleton

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.GetResult

import scala.concurrent.Future

case class UserAuth(email:String, passwdHash:String, creationTime:Long)

@Singleton
class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {


  import profile.api._

  implicit val getUserResult = GetResult(r => UserAuth(r.nextString, r.nextString, r.nextLong()))


  def getUserByEmail(email:String): Future[Option[UserAuth]] = {
    db.run(sql"select email, passwdHash, creationTime from users where email = $email".as[UserAuth].headOption)
  }

  def createUser(user:UserAuth): Future[Int] = {
    db.run(sqlu"insert into users (email, passwdHash, creationTime) values (${user.email}, ${user.passwdHash}, ${user.creationTime})")
  }

  def getAllUsers: Future[Vector[String]] = {

    db.run(sql"select email from users".as[String])
  }

}
