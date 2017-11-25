import scala.concurrent.duration.Duration

package object model {

  case class Country(title: String, id: Option[Long] = None)
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
  case class Staff(name: String, rate: Double, age: Int, id: Option[Long] = None)

  case class Genre(title: String, description: Option[String], id: Option[Long] = None)
}