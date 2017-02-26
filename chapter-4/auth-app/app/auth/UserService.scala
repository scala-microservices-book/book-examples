package auth

import javax.inject.{Inject, Singleton}

import com.microservices.auth.User
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(userDao: UserDao) {
  def userExists(email: String)(implicit exec:ExecutionContext): Future[Boolean] = {
    for {
      user <- userDao.getUserByEmail(email)
    } yield {
      user.isDefined
    }
  }

  def addUser(user: User)(implicit exec: ExecutionContext): Future[Int] = {
    userExists(user.email).flatMap(userExists => if (userExists)
      throw new IllegalArgumentException("User already exists")
    else userDao.createUser(UserAuth(user.email, hashPassword(user.password), System.currentTimeMillis())))
  }

  def validateUser(email:String, password:String)(implicit exec:ExecutionContext): Future[Boolean] = {
    userDao.getUserByEmail(email).map {
      case Some(auth) => checkPassword(password, auth.passwdHash)
      case None => false
    }
  }

  def getAllUserNames: Future[Vector[String]] = userDao.getAllUsers

  private def hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(11))

  private def checkPassword(password: String, passwordHash: String): Boolean = BCrypt.checkpw(password, passwordHash)
}
