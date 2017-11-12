package week4.slick.workshop.models

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class Film(title: String,
                duration: Duration,
// n-to-n relations should be implemented via junction tables
//                genre: Genre,
//                cast: Staff,
//                country: Country,
// because only one director Film - Director(Staff) is 1 - n
                directorId: Long,
                rating: Double,
                id: Option[Long] = None)

final class FilmTable(tag: Tag) extends Table[Film](tag, "film") {

//  def id = column[Option[Long]]("id", O.PrimaryKey)
  def title = column[String]("title")
  def duration = column[Duration]("duration")
  def directorId = column[Long]("director_id")
  def rating = column[Double]("rating")
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  val directorFk =
    foreignKey("director_id_fk", directorId, TableQuery[StaffTable])(_.id)

//  def * = (id.?, title, duration, directorId, rating).mapTo[Film]
  def * =
//    (id, title, duration, directorId, rating) <> (Film.apply _ tupled, Film.unapply)
    (title, duration, directorId, rating, id.?) <> (Film.apply _ tupled, Film.unapply)

}

object FilmTable {
  val table = TableQuery[FilmTable]
}

case class FilmToGenre(id: Option[Long], filmId: Long, genreId: Long)

final class FilmToGenreTable(tag: Tag)
    extends Table[FilmToGenre](tag, "film_to_genre") {

  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  val filmId = column[Long]("film_id")
  val genreId = column[Long]("genre_id")

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable])(_.id)
  val genreIdFk =
    foreignKey("genre_id_fk", genreId, TableQuery[GenreTable])(_.id)

  def * =
    (id.?, filmId, genreId) <> (FilmToGenre.apply _ tupled, FilmToGenre.unapply)
}

object FilmToGenreTable {
  val table = TableQuery[FilmToGenreTable]
}

case class FilmToStaff(filmId: Long, staffId: Long, id: Option[Long])

final class FilmToStaffTable(tag: Tag)
    extends Table[FilmToStaff](tag, "film_to_cast") {

  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  val filmId = column[Long]("film_id")
  val staffId = column[Long]("staff_id")

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable])(_.id)
  val staffIdFk =
    foreignKey("staff_id_fk", staffId, TableQuery[StaffTable])(_.id)

  def * =
    (filmId, staffId, id.?) <> (FilmToStaff.apply _ tupled, FilmToStaff.unapply)
}

object FilmToStaffTable {
  val table = TableQuery[FilmToStaffTable]
}

case class FilmToCountry(filmId: Long, countryId: Long, id: Option[Long])

final class FilmToCountryTable(tag: Tag)
    extends Table[FilmToCountry](tag, "film_to_country") {

  val filmId = column[Long]("film_id")
  val countryId = column[Long]("country_id")
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable])(_.id)
  val countryIdFk =
    foreignKey("country_id_fk", countryId, TableQuery[CountryTable])(_.id)

  def * =
    (filmId, countryId, id.?) <> (FilmToCountry.apply _ tupled, FilmToCountry.unapply)
}

object FilmToCountryTable {
  val table = TableQuery[FilmToCountryTable]
}

class FilmRepository(db: Database) {
  val filmTableQuery = FilmTable.table

  def create(film: Film,
             genresIds: List[Long],
             actorIds: List[Long],
             countryIds: List[Long]): Future[Film] = {
    val query = (filmTableQuery returning filmTableQuery += film).flatMap {
      insertedFilm =>
        (FilmToGenreTable.table ++= genresIds.map(genreId =>
          FilmToGenre(None, insertedFilm.id.get, genreId)))
          .andThen(FilmToCountryTable.table ++= countryIds.map(countryId =>
            FilmToCountry(insertedFilm.id.get, countryId, None)))
          .andThen(FilmToStaffTable.table ++= actorIds.map(actorId =>
            FilmToStaff(insertedFilm.id.get, actorId, None)))
          .andThen(DBIO.successful(insertedFilm))
    }
    db.run(query)
  }

  def getAllFilmIndoById(filmId: Long): Future[(Film, Seq[Genre], Seq[Country], Seq[Staff])] = {
    val query = for {
      // will return Seq anyway, call head here
      film <- FilmTable.table.filter(_.id === filmId).result.head
      genres <- GenreTable.table
        .filter(
          _.id in
            FilmToGenreTable.table.filter(_.filmId === filmId).map(_.genreId))
        .result
      countries <- CountryTable.table
        .filter(_.id in
          FilmToCountryTable.table.filter(_.filmId === filmId).map(_.countryId))
        .result
      actors <- StaffTable.table
        .filter(
          _.id in
            FilmToStaffTable.table.filter(_.filmId === filmId).map(_.staffId))
        .result
    } yield (film, genres, countries, actors)

    db.run(query)
  }

  def getFilmWithDirector(filmId: Long): Future[(Film, Staff)] = {
    val query = FilmTable.table join StaffTable.table on (_.directorId === _.id)
    db.run(
      query.result.head
    )
  }
}
