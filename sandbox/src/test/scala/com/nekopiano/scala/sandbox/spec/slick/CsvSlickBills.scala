package com.nekopiano.scala.sandbox.spec.slick

import java.util.Date

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.nekopiano.scala.sandbox.spec.slick.CsvH2Driver.api._
//import slick.driver.H2Driver.api._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._

//import com.github.tototoshi.slick.H2JodaSupport._
//import H2CsvJodaSupport._

object CsvSlickBills extends App {

  val dt = DateTime.now
  val b = dt > DateTime.now



  // the base query for the Users table
  val bills = TableQuery[Bills]

  val db = Database.forConfig("h2mem1")
  try {
    Await.result(db.run(DBIO.seq(

      // print the users (select * from USERS)
      bills.result.map(println)

    )), Duration.Inf)

    Await.result(db.run(DBIO.seq(
      bills
        .filter(row => (row.name like "% %"))
        //.map(row => (row.id, row.month, row.name))
        .result.map(println)

    )), Duration.Inf)

    Await.result(db.run(DBIO.seq(
      bills
        //.filter(row => {row.month})
        //.filter(row => {row.month < DateTime.now})
        //.filter(row => {row.month > new java.sql.Date(11111L)})
        //.map(row => (row.id, row.month, row.name))
        .result.map(println)

    )), Duration.Inf)

  } finally db.close
}

//case class Bill(id: Option[Int] = None, month:java.sql.Date, name: String)
//case class Bill(id: Option[Int] = None, month:Date, name: String)
case class Bill(id: Option[Int] = None, month:DateTime, name: String)

//class Bills(tag: Tag) extends Table[Bill](tag, "csvread('./SalesResultData-201105-201607_en.csv')") {
class Bills(tag: Tag) extends Table[Bill](tag, "csvread('./bills.csv')") {
//class Bills(tag: Tag) extends Table[Bill](tag, "csvread('./SalesResultData-201105-201608_en.csv')") {

  implicit val dateTime2StringMapper = JapaneseDateMapper.dateTime2StringMapper

  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def month = column[DateTime]("MONTH")
  //def month = column[DateTime]("MONTH")(JapaneseDateMapper.dateTime2StringMapper)
  //def month = column[Date]("MONTH")(JapaneseDateMapper.utilDate2StringMapper)
  //def month = column[java.sql.Date]("MONTH")(JapaneseDateMapper.sqlDate2StringMapper)

  // The name can't be null
  def name = column[String]("NAME")
  // the * projection (e.g. select * ...) auto-transforms the tupled

  import JapaneseDateMapper._

  def * = (id.?, month, name) <> (Bill.tupled, Bill.unapply)
}


object JapaneseDateMapper {
  //val dateFormatter = DateTimeFormat.forPattern("yyyy/MM/dd")
  val dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

  implicit val dateTime2StringMapper = MappedColumnType.base[DateTime, String](
    { dateTime => dateFormatter.print(dateTime) },
    { dateString => dateFormatter.parseDateTime(dateString) })

  implicit val utilDate2StringMapper = MappedColumnType.base[Date, String](
    { utilDate => dateFormatter.print(new DateTime(utilDate)) },
    { dateString => dateFormatter.parseDateTime(dateString).toDate })

  implicit val sqlDate2StringMapper = MappedColumnType.base[java.sql.Date, String](
    { sqlDate => dateFormatter.print(sqlDate.getTime) },
    { dateString => new java.sql.Date(dateFormatter.parseDateTime(dateString).getMillis) })

}
