package week4.slick.workshop.models

import scala.concurrent.duration.Duration
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

// id as Option - autoincrement
case class Film(id: Option[Long],
                title: String,
                duration: Duration,
                // n-to-n is implemented via junction tables
//                genre: Genre,
//                cast: Staff,
//               country: Country,
                // because only one director
                directorId: Long,
                rating: Double)

final class FilmTable(tag: Tag) extends Table[Film](tag, "film") {

  def id = column[Option[Long]]("id", O.PrimaryKey)
  def title = column[String]("title")
  def duration = column[Duration]("duration")
  def directorId = column[Long]("director_id")
  def rating = column[Double]("rating")

//  def * = (sender, content, id).mapTo[Message]
  def * = (id, title, duration, directorId, rating) <> (Film.apply _ tupled, Film.unapply)

}
