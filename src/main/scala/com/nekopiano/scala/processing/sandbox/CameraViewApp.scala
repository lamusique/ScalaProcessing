package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPVector, ScalaPApplet}

/**
 * Created by neko on 2015/11/29.
 */
class CameraViewApp extends ScalaPApplet {

  override def settings: Unit = {
    size(1024, 768, P3D)
    // the following can't work because here the real window size isn't defined yet.
    // size(width, height, P3D)
  }
  override def setup: Unit = {

  }
  override def draw: Unit = {
    background(225)

    //translate(width/2, height/2, 0);

    val eye = ScalaPVector(90,-100,300)
    val center = ScalaPVector(0,0,0)

    usingStyle {
      fill(10)
      strokeWeight(5)
      point(eye)
      point(center)
    }
    camera(eye, center)

    translate(mouseX,mouseY,-mouseY)
    box(150)
  }

}
object CameraViewApp {
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
