import java.util.Date

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import slick.driver.H2Driver.api._

import com.github.tototoshi.slick.H2JodaSupport._


object CaseClassMapping extends App {

  // the base query for the Users table
  val users = TableQuery[Users]

  val db = Database.forConfig("h2mem1")
  try {
    Await.result(db.run(DBIO.seq(
      // create the schema
      users.schema.create,

      // insert two User instances
      users += User("John Doe"),
      users += User("Fred Smith"),

      // print the users (select * from USERS)
      users.result.map(println)
    )), Duration.Inf)
  } finally db.close
}

case class User(name: String, id: Option[Int] = None, createDate:DateTime = new DateTime, updateDate:Date = new Date)

class Users(tag: Tag) extends Table[User](tag, "USERS") with BritishDateMapper {
  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  // The name can't be null
  def name = column[String]("NAME")
  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User

  //implicit val utilDate2StringMapper = BritishDateMapper.utilDate2StringMapper
  implicit val utilDate2StringMapperLocal = utilDate2StringMapper
  //import BritishDateMapper._
  //implicit val bdm = BritishDateMapper

  def createDate = column[DateTime]("CREATE_DATETIME")

  //def updateDate = column[Date]("UPDATE_DATETIME")(BritishDateMapper.utilDate2StringMapper)
  //def updateDate = column[Date]("UPDATE_DATETIME")
  def updateDate = column[Date]("UPDATE_DATETIME")(utilDate2StringMapper)

  def * = (name, id.?, createDate, updateDate) <> (User.tupled, User.unapply)
}

trait BritishDateMapper {
  val dateFormatter = DateTimeFormat.forPattern("yyyy/MM/dd")

  val dateTime2StringMapper = MappedColumnType.base[DateTime, String](
    { dateTime => dateFormatter.print(dateTime) },
    { dateString => dateFormatter.parseDateTime(dateString) })

  implicit val utilDate2StringMapper = MappedColumnType.base[Date, String](
    { utilDate => dateFormatter.print(new DateTime(utilDate)) },
    { dateString => dateFormatter.parseDateTime(dateString).toDate })

}
