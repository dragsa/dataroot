package week4.slick.workshop.models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

// id as Option - autoincrement
case class Country(id: Option[Long], title: String)

final class CountryTable(tag: Tag) extends Table[Country](tag, "countries") {
  // no O.AutoInc
//  val id = column[Option[Long]]("id", O.PrimaryKey)
  val id = column[Long]("id", O.PrimaryKey)
  val title = column[String]("table")

//  def * = (id, title).mapTo[Country]
//  def * = (id, title) <> (Country.apply _ tupled, Country.unapply)
  def * = (id.?, title) <> (Country.apply _ tupled, Country.unapply)
}
