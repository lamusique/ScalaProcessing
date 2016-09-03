package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppCompanion, ScalaPVector, TwoDimensionalPApp}

/**
  * Created on 03/09/2016.
  */
class ReplApp extends TwoDimensionalPApp {
//  val value = 11
//  var variable = 22

  import ReplApp._

  override def settings: Unit = {
    size(800, 600)
  }

  override def draw: Unit = {
    background(0)

    val second = System.currentTimeMillis() % 1000
    text("second=" + second, width / 2, height / 3)
    if (second > 250) {
      text("value=" + value, width / 2, height / 2)
      text("variable=" + variable, width / 2, height / 1.5f)
    }

  }

}
object ReplApp extends ScalaPAppCompanion {
  val value = 11
  var variable = 22
}
