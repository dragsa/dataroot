package week4.slick.workshop.models

import scala.concurrent.duration.Duration
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

// id as Option - autoincrement
case class Film(id: Option[Long],
                title: String,
                duration: Duration,
                // n-to-n should ne implemented via junction tables
//                genre: Genre,
//                cast: Staff,
//                country: Country,
                // because only one director
                directorId: Long,
                rating: Double)

final class FilmTable(tag: Tag) extends Table[Film](tag, "film") {

//  def id = column[Option[Long]]("id", O.PrimaryKey)
  def id = column[Long]("id", O.PrimaryKey)
  def title = column[String]("title")
  def duration = column[Duration]("duration")
  def directorId = column[Long]("director_id")
  def rating = column[Double]("rating")

  val directorFk = foreignKey("director_id_fk", directorId, TableQuery[StaffTable]) (_.id)

//  def * = (sender, content, id).mapTo[Message]
  def * =
//    (id, title, duration, directorId, rating) <> (Film.apply _ tupled, Film.unapply)
    (id.?, title, duration, directorId, rating) <> (Film.apply _ tupled, Film.unapply)

}

case class FilmToGenre(id: Option[Long], filmId: Long, genreId: Long)

final class FilmToGenreTable(tag: Tag)
    extends Table[FilmToGenre](tag, "film_to_genre") {

  val id = column[Long]("id", O.PrimaryKey)
  val filmId = column[Long]("film_id")
  val genreId = column[Long]("genre_id")

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable]) (_.id)
  val genreIdFk = foreignKey("genre_id_fk", genreId, TableQuery[GenreTable]) (_.id)

  def * =
    (id.?, filmId, genreId) <> (FilmToGenre.apply _ tupled, FilmToGenre.unapply)
}

case class FilmToStaff(id: Option[Long], filmId: Long, staffId: Long)

final class FilmToStaffTable(tag: Tag)
    extends Table[FilmToStaff](tag, "film_to_cast") {

  val id = column[Long]("id", O.PrimaryKey)
  val filmId = column[Long]("film_id")
  val staffId = column[Long]("staff_id")

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable]) (_.id)
  val staffIdFk = foreignKey("staff_id_fk", staffId, TableQuery[StaffTable]) (_.id)

  def * =
    (id.?, filmId, staffId) <> (FilmToStaff.apply _ tupled, FilmToStaff.unapply)
}

case class FilmToCountry(id: Option[Long], filmId: Long, countryId: Long)

final class FilmToCountryTable(tag: Tag)
    extends Table[FilmToCountry](tag, "film_to_country") {

  val id = column[Long]("id", O.PrimaryKey)
  val filmId = column[Long]("film_id")
  val countryId = column[Long]("country_id")

  val filmIdFk = foreignKey("film_id_fk", filmId, TableQuery[FilmTable]) (_.id)
  val countryIdFk = foreignKey("staff_id_fk", countryId, TableQuery[CountryTable]) (_.id)

  def * =
    (id.?, filmId, countryId) <> (FilmToCountry.apply _ tupled, FilmToCountry.unapply)
}
