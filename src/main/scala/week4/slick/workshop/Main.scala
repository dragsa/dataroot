package week4.slick.workshop

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random
import slick.jdbc.PostgresProfile.api._
import implicits._
import model._

object Main extends App {
  val db = Database.forURL(
    "jdbc:postgresql://localhost:5432/filmotech?user=postgres&password=postgres")

  val countryRepository = new CountryRepository(db)
  val genreRepository = new GenreRepository(db)
  val staffRepository = new StaffRepository(db)
  val filmRepository = new FilmRepository(db)

  def init() = {
    Await.result(db.run(CountryTable.table.schema.create), Duration.Inf)
    Await.result(db.run(StaffTable.table.schema.create), Duration.Inf)
    Await.result(db.run(GenreTable.table.schema.create), Duration.Inf)
    Await.result(db.run(FilmTable.table.schema.create), Duration.Inf)
    Await.result(db.run(FilmToCountryTable.table.schema.create), Duration.Inf)
    Await.result(db.run(FilmToGenreTable.table.schema.create), Duration.Inf)
    Await.result(db.run(FilmToStaffTable.table.schema.create), Duration.Inf)
  }

  def databaseFill(): Unit = {
    for (i <- 1 to 5) {
      Await.result(countryRepository.create(Country(s"Country $i")),
                   Duration.Inf)
    }

    val genres = List("Horror", "Sci-fi", "Drama", "Fantasy", "18+")
    for (i <- genres) {
      Await.result(genreRepository.create(Genre(i, None)),
        Duration.Inf)
    }

    for (i <- 1 to 100) {
      Await.result(staffRepository.create(
                     Staff(s"Staff Name $i", i * 10000, i % 10 + 25)),
                   Duration.Inf)
    }

    for (i <- 1 to 10) {
      Await.result(
        filmRepository.create(
          Film(s"Film Name $i", (i * 10000).seconds, i % 5 * 10 + 1, i % 5 + 5),
          List(Random.nextInt(genres.length) + 1, Random.nextInt(genres.length) + 1),
          List(Random.nextInt(100).toLong + 1, Random.nextInt(100).toLong + 1),
          List(Random.nextInt(5).toLong + 1, Random.nextInt(5).toLong + 1)
        ),
        Duration.Inf)
    }
  }

//  init()
//  databaseFill()
  val allFilmInfo = filmRepository.getAllFilmInfoById(1)
  Await.result(allFilmInfo, Duration.Inf)
  println(allFilmInfo)

  val filmWithDirector = filmRepository.getFilmWithDirector(1)
  Await.result(filmWithDirector, Duration.Inf)
  println(filmWithDirector)
}
