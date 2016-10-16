package com.nekopiano.scala.processing.sandbox.poc.pdf

import com.nekopiano.scala.processing.{ScalaPApplet, ScalaPVector}
import processing.core.PGraphics;

/**
 * Created on 26/07/2016.
 */
class LineRenderingApp extends ScalaPApplet {

  var pdf:PGraphics = null
  var record = false

  override def settings(): Unit = {
    size(200, 200)
  }

  override def setup(): Unit = {
    background(255)
    smooth()
    strokeWeight(15)
    frameRate(24)
  }

  var startX = 0

  override def draw(): Unit = {

    stroke(random(50), random(255), random(255), 100)
    line(startX, 0, random(0, width), height)
    if (startX < width) {
        startX += 1
    } else {
        startX = 0
    }

  }


}


object LineRenderingApp {
  val BOOTING_CLASS_NAME = this.getClass.getName.dropRight(1)

  def main(args: Array[String]) {
    // This specifies the class to be instantiated.
    val appletArgs = Array(BOOTING_CLASS_NAME)
    if (args != null) {
      ScalaPApplet.main(appletArgs ++ args)
    } else {
      ScalaPApplet.main(appletArgs)
    }
  }
}
