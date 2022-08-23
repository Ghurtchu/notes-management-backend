package db

import io.github.nremond.PBKDF2
import io.github.nremond.legacy.SecureHash
import model.{Note, User}
import util.hash.{PasswordHashService, SecureHashService}
import zio.json.*

import java.nio.charset.StandardCharsets
import scala.collection.mutable.{ArrayBuffer, ListBuffer, Map}

object InMemoryDB {

  val passwordHashService: PasswordHashService = SecureHashService()

  lazy val users: scala.collection.mutable.Map[Int, User] =
    scala.collection.mutable.Map[Int, User]()

  users.addAll(readUsersAndHashPasswords.zipWithIndex.map {
    case (k, v) => (v + 1, k)
  })

  def readUsersAndHashPasswords: List[User] = {
    val bufferedSource = scala.io.Source.fromFile("src/main/resources/users.json")
    val data: String = bufferedSource.getLines().mkString("\n")
    val usersEither = data.fromJson[List[User]]

    bufferedSource.close()

    usersEither.fold(
      _ => List(),
      users => users.map {
        user => user.copy(password = passwordHashService.hash(user.password))
      }
    )
  }

  lazy val notes: ListBuffer[Note] = ListBuffer(
    Note(1, "Z title", "first note body", "2020-01-01", users(1).id.get),
    Note(2, "L title", "second note body", "2020-01-02", users(1).id.get),
    Note(3, "J title", "third note body", "2020-01-03", users(1).id.get),
    Note(4, "F title", "fourth note body", "2021-04-03", users(2).id.get),
    Note(5, "A title", "fifth note body", "2021-05-03", users(2).id.get),
    Note(6, "B title", "sixth note body", "2021-06-03", users(3).id.get),
    Note(7, "C title", "seventh note body", "2021-06-03", users(4).id.get),
    Note(8, "D title", "eighth note body", "2021-06-03", users(4).id.get),
  )

}

