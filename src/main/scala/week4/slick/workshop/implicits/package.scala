package week4.slick.workshop

import scala.concurrent.duration._
import slick.jdbc.PostgresProfile.api._

package object implicits {
    implicit val durationToLongMapper = MappedColumnType.base[Duration, Long](_.toSeconds, _.seconds)
}
