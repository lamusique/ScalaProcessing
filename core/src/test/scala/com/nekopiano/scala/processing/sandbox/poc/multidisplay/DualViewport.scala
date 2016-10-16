package com.nekopiano.scala.processing.sandbox.poc.multidisplay

import com.nekopiano.scala.processing.{ScalaPAppletCamera, ScalaPApplet}
import processing.core.PGraphics

/**
 * Created on 2016/06/26.
 */
class DualViewportApp extends ScalaPApplet {

  implicit val sp5 = this

  val cameraView = new ScalaPAppletCamera

  val w = 500
  val h = 300

  var leftViewport: PGraphics = null
  var rightViewport: PGraphics = null

  override def settings: Unit = {

    //size(1280, 768, P3D)
    size(w, h, P3D)

    // for Retina
    pixelDensity(2)
  }
  override def setup: Unit = {
    cameraView.initialize()

    leftViewport = createGraphics(w/2, h, P3D);
    rightViewport = createGraphics(w/2, h, P3D);
  }
  override def draw: Unit = {

      //draw something fancy on every viewports
      leftViewport.beginDraw();
      leftViewport.background(102);
      leftViewport.stroke(255);
      leftViewport.line(40, 40, mouseX, mouseY);
      leftViewport.endDraw();

      rightViewport.beginDraw();
      rightViewport.background(102);
      rightViewport.stroke(255);
      rightViewport.line(40, 40, mouseX, mouseY);
      rightViewport.endDraw();

      //add the two viewports to your main panel
      image(leftViewport, 0, 0);
      image(rightViewport, w/2, 0);

  }


}

object DualViewportApp {
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
