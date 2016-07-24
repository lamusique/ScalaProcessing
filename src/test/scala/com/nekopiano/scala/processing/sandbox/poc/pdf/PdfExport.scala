package com.nekopiano.scala.processing.sandbox.poc.pdf;

import com.nekopiano.scala.processing.{ScalaPApplet, ScalaPVector}
import processing.core.PGraphics;

/**
 * Created on 24/07/2016.
 */
class PdfExportApp extends ScalaPApplet {

  var pdf:PGraphics = null
  var record = false

  override def settings(): Unit = {
    //size(800, 600, P2D)
    size(800, 600, P3D)
    //noLoop()
    //size(800, 600, PDF, "processing-sample.pdf")

    //size(800, 600, SVG, "processing-sample.svg")
    //beginRecord(PDF, "processing-sample.pdf")
  }

  override def setup(): Unit = {

  }

  override def draw(): Unit = {

    if (record) {
      beginRaw(PDF, "output.pdf");
    }


    background(220)

    stroke(10)
    strokeWeight(100)
    line(ScalaPVector.origin, ScalaPVector(150, 220))

    fill(0)
    rect(60, 80, 102, 76)

    stroke(200)
    beginShape(TRIANGLE_STRIP)
    vertex(30, 75)
    vertex(40, 20)
    vertex(50, 75)
    vertex(60, 20)
    vertex(70, 75)
    vertex(80, 20)
    vertex(90, 75)
    endShape()

    text("SAMPLE Text", ScalaPVector(500, 500))

    translate(width/2, height/2, -200)
    rotateZ(0.2F)
    rotateY(mouseX/500.0F)
    box(100)

    //exit()
    //endRecord()
    if (record) {
      endRaw();
      record = false;
    }
  }


  // Hit 'r' to record a single frame
  override def keyPressed() {
    if (key == 'r') {
      record = true;
    }
  }

}


object PdfExportApp {
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

class Objet {


}