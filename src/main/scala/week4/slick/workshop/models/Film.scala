package week4.slick.workshop.models

import scala.concurrent.duration.Duration

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
