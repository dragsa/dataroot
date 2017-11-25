package daos

import scala.concurrent.Future
import model.Staff
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._


final class StaffTable(tag: Tag) extends Table[Staff](tag, "staff") {
  //  val id = column[Option[Long]]("id", O.PrimaryKey)
  val name = column[String]("name")
  val rate = column[Double]("rate")
  val age = column[Int]("age")
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  //  def * = (id, title).mapTo[Country]
//  def * = (id, name, rate, age) <> (Staff.apply _ tupled, Staff.unapply)
  def * = (name, rate, age, id.?) <> (Staff.apply _ tupled, Staff.unapply)
}

object StaffTable {
  val table = TableQuery[StaffTable]
}

class StaffRepository(db: Database) {
  val staffTableQuery = StaffTable.table

  def create(staff: Staff): Future[Staff] = {
    db.run(staffTableQuery returning staffTableQuery += staff)
  }
}