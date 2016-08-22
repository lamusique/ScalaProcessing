package com.nekopiano.scala.sandbox.spec.csv

import java.io.PrintWriter

/**
  * Created on 17/08/2016.
  */
object CsvQueryTest extends App {

  import scalikejdbc._
  import csvquery._


  implicit val session = autoCSVSession


  Some(new PrintWriter("./sample-test.csv")).foreach{p => p.write("Alice,23\nBob,34\nChris,30"); p.close}

  val csv = CSV("./sample-test.csv", Seq("name", "age"))

  val count: Long = withCSV(csv) { table =>
    sql"select count(*) from $table".map(_.long(1)).single.apply().get
  }
  println(count)

  val records: Seq[Map[String, Any]] = withCSV(csv) { table =>
    sql"select * from $table".toMap.list.apply()
  }
  println(records)

}

