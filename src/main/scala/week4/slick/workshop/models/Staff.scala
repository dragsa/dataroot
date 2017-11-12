package week4.slick.workshop.models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

// id as Option - autoincrement
case class Staff(id: Option[Long], name: String, rate: Double, age: Int)

final class StaffTable(tag: Tag) extends Table[Staff](tag, "staff") {
  // no O.AutoInc
  val id = column[Option[Long]]("id", O.PrimaryKey)
  val name = column[String]("name")
  val rate = column[Double]("rate")
  val age = column[Int]("age")

  //  def * = (id, title).mapTo[Country]
  def * = (id, name, rate, age) <> (Staff.apply _ tupled, Staff.unapply)
}