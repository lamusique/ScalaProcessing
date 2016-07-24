package com.nekopiano.scala.processing.sandbox.poc.multidisplay

import com.nekopiano.scala.processing.{Camera, ScalaPApplet}
import processing.core.{PApplet, PGraphics}

/**
 * Created on 2016/07/24.
 */
class DualWindowApp extends ScalaPApplet {

  implicit val sp5 = this

  val w = 500
  val h = 300

  var leftViewport: PGraphics = null
  var rightViewport: PGraphics = null

  override def settings: Unit = {

    //size(1280, 768, P3D)
    size(w, h, P3D)
    //size(100, 100);

    // for Retina
    pixelDensity(2)
  }
  override def setup: Unit = {

    val args = Array("YourSketchNameHere")
    val sa = new SecondApplet()
    PApplet.runSketch(args, sa)
  }
  override def draw: Unit = {
    background(0);
    ellipse(50, 50, 10, 10);
  }

}

object DualWindowApp {
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

class SecondApplet extends PApplet {

  override def settings() {
    size(200, 100);
  }
  override def draw() {
    background(255);
    fill(0);
    ellipse(100, 50, 10, 10);
  }

}
