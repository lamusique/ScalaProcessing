
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.nekopiano.scala.sandbox.spec.slick.CsvH2Driver.api._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
//import slick.driver.H2Driver.api._

object CsvSlick extends App {


  Some(new PrintWriter("./sample-test.csv")).foreach{p => p.write("id,name,age,dob\n1,Alice,23,\"19950822\"\n2,Bob,34,\"19950411\"\n3,Chris,30,\"19960222\""); p.close}


  // the base query for the Users table
  val rows = TableQuery[Rows]

  val db = Database.forConfig("h2mem1")
  try {
    Await.result(db.run(DBIO.seq(

      // print the users (select * from USERS)
      rows.result.map(println)

    )), Duration.Inf)

    Await.result(db.run(DBIO.seq(
      rows
        .filter(row => (row.name like "%i%"))
        //.map(row => (row.id, row.name))
        .result.map(println)

    )), Duration.Inf)


  } finally db.close
}

case class Row(id: Option[Int] = None, dob:Date, name: String, age:Short)

class Rows(tag: Tag) extends Table[Row](tag, "csvread('./sample-test.csv')") {
  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def dob = column[Date]("DOB")(SystemDateMapper.utilDate2StringMapper)
  // The name can't be null
  def name = column[String]("NAME")
  def age = column[Short]("AGE")

  // the * projection (e.g. select * ...) auto-transforms the tupled

  import SystemDateMapper._

  def * = (id.?, dob, name, age) <> (Row.tupled, Row.unapply)
}



object SystemDateMapper {
  val dateFormatter = DateTimeFormat.forPattern("yyyyMMdd")

  implicit val dateTime2StringMapper = MappedColumnType.base[DateTime, String](
    { dateTime => dateFormatter.print(dateTime) },
    { dateString => dateFormatter.parseDateTime(dateString) })

  implicit val utilDate2StringMapper = MappedColumnType.base[Date, String](
    { utilDate => dateFormatter.print(new DateTime(utilDate)) },
    { dateString => dateFormatter.parseDateTime(dateString).toDate })

}
