package com.nekopiano.scala.sandbox.spec.slick

import slick.driver.H2Driver

/**
  * Created on 18/08/2016.
  */
trait CsvH2Driver extends H2Driver {
  override def quoteIdentifier(id: String) = id
}
object CsvH2Driver extends CsvH2Driver {
}
