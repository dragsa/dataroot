package week4.slick.workshop.models

import scala.concurrent.Future
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

// id as Option - autoincrement
case class Genre(id: Option[Long], title: String, description: Option[String])

final class GenreTable(tag: Tag) extends Table[Genre](tag, "genre") {
  // no O.AutoInc
//  val id = column[Option[Long]]("id", O.PrimaryKey)
  val id = column[Long]("id", O.PrimaryKey)
  val title = column[String]("title")
  val description = column[Option[String]]("description")

//    def * = (id, title, description).mapTo[Genre]
//  def * = (id, title, description) <> (Genre.apply _ tupled, Genre.unapply)
  def * = (id.?, title, description) <> (Genre.apply _ tupled, Genre.unapply)
}

object GenreTable {
  val table = TableQuery[GenreTable]
}

class GenreRepository(db: Database) {
  val genreTableQuery = GenreTable.table

  def create(genre: Genre): Future[Genre] = {
    db.run(genreTableQuery returning genreTableQuery += genre)
  }
}