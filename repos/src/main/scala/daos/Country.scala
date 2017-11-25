package daos

import scala.concurrent.Future
import model.Country
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

class CountryTable(tag: Tag) extends Table[Country](tag, "countries") {
//  val id = column[Option[Long]]("id", O.PrimaryKey)
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  val title = column[String]("table")

//  def * = (id, title).mapTo[Country]
//  def * = (id, title) <> (Country.apply _ tupled, Country.unapply)
  def * = (title, id.?) <> (Country.apply _ tupled, Country.unapply)
}

object CountryTable {
  val table = TableQuery[CountryTable]
}

class CountryRepository(db: Database) {
  val countryTableQuery = CountryTable.table

  def create(country: Country): Future[Country] = {
    db.run(countryTableQuery returning countryTableQuery += country)
  }

  def update(country: Country): Future[Int] = {
    db.run(countryTableQuery.filter(_.id === country.id).update(country))
  }

  def delete(countryId: Long): Future[Int] = {
    db.run(countryTableQuery.filter(_.id === countryId).delete)
  }

  def getById(countryId: Long): Future[Option[Country]] = {
    db.run(countryTableQuery.filter(_.id === countryId).result.headOption)
  }
}