package com.nekopiano.scala.sandbox.spec.slick

import slick.ast.TableNode
import slick.driver.H2Driver

/**
  * Created on 18/08/2016.
  */
trait CsvH2Driver extends H2Driver {

  override def quoteTableName(t: TableNode): String = {

    if(t.tableName.contains("csvread")) return t.tableName

    t.schemaName match {
    case Some(s) => quoteIdentifier(s) + "." + quoteIdentifier(t.tableName)
    case None => quoteIdentifier(t.tableName)
    }
  }
}
object CsvH2Driver extends CsvH2Driver {
}


object H2CsvJodaSupport extends com.github.tototoshi.slick.GenericJodaSupport(CsvH2Driver)
