package week4.slick.workshop

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import slick.jdbc.PostgresProfile.api._
import models._

object Main extends App {
  val db = Database.forURL(
    "jdbc:postgresql://localhost:5432/filmotech?user=postgres&password=postgres")

  Await.result(db.run(CountryTable.table.schema.create), Duration.Inf)
  Await.result(db.run(StaffTable.table.schema.create), Duration.Inf)
  Await.result(db.run(GenreTable.table.schema.create), Duration.Inf)
  Await.result(db.run(FilmTable.table.schema.create), Duration.Inf)
  Await.result(db.run(FilmToCountryTable.table.schema.create), Duration.Inf)
  Await.result(db.run(FilmToGenreTable.table.schema.create), Duration.Inf)
  Await.result(db.run(FilmToStaffTable.table.schema.create), Duration.Inf)

}
