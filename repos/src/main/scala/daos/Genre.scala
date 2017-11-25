package daos

import scala.concurrent.Future
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import model.Genre

class GenreTable(tag: Tag) extends Table[Genre](tag, "genre") {
  //  val id = column[Option[Long]]("id", O.PrimaryKey)
  val title = column[String]("title")
  val description = column[Option[String]]("description")
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

//    def * = (id, title, description).mapTo[Genre]
//  def * = (id, title, description) <> (Genre.apply _ tupled, Genre.unapply)
  def * = (title, description, id.?) <> (Genre.apply _ tupled, Genre.unapply)
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