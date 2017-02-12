package com.nekopiano.scala.processing.sandbox.poc.pdf;

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPApplet, ScalaPVector, TwoDimensionalPApp}
import processing.core.PGraphics
import processing.event.MouseEvent;

/**
 * Created on 24/07/2016.
 */
class PdfExportApp extends TwoDimensionalPApp {

  var record = false

  override def settings(): Unit = {
    //size(800, 600, P3D)
    //size(800, 600, P2D)
    size(800, 600)

    //smooth()
    // for Retina
    pixelDensity(2)

    //noLoop()
    //size(800, 600, PDF, "processing-sample.pdf")

    //size(800, 600, SVG, "processing-sample.svg")
    //beginRecord(PDF, "processing-sample.pdf")
  }

  override def setup(): Unit = {
    background(250)

  }

  var startX = 0

  override def draw(): Unit = {

    if (record) {
      //beginRaw(PDF, "output.pdf");
      beginRecord(PDF, "output.pdf");
    }

    scale(scale)

    background(250)

    fill(0)
    //rect(60, 80, 102, 76)

//    rect(30, 20, 50, 50)
//    scale(0.5f)
//    rect(30, 20, 50, 50)
//    scale(1f)


//    stroke(200)
//    beginShape(TRIANGLE_STRIP)
//    vertex(30, 75)
//    vertex(40, 20)
//    vertex(50, 75)
//    vertex(60, 20)
//    vertex(70, 75)
//    vertex(80, 20)
//    vertex(90, 75)
//    endShape()

    //stroke(10,10,10,100)
    stroke(50)

    //strokeWeight(10)

    // When line() w/ the 3rd dimension in 2D, you'll get the following warning:
    // vertex() with x, y, and z coordinates can only be used with a renderer that supports 3D, such as P3D. Use a version without a z-coordinate instead.
    // and JAVA2D or PDF doesn't display that line.
    line(ScalaPVector.origin, ScalaPVector(300, 450))
    line(ScalaPVector(width, height), ScalaPVector(230, 400))

    stroke(random(50), random(255), random(255), 100)
    line(startX, 0, random(0, width), height)
    if (startX < width) {
      startX += 1
    } else {
      startX = 0
    }

    text("SAMPLE Text", ScalaPVector(500, 500))
    text("scale=" + scale, ScalaPVector(width/2, height/2))

//    translate(width/2, height/2, -200)
//    rotateZ(0.2F)
//    rotateY(mouseX/500.0F)
//    box(100)



    //exit()
    //endRecord()
    if (record) {
      //endRaw()
      endRecord()
      record = false;
    }
  }


  // Hit 'r' to record a single frame
  override def keyPressed() {
    if (key == 'r') {
      record = true;
    }
  }

  var scale = 1f

  override def mouseWheel(event:MouseEvent) {
    val amount = event.getCount() / 100f
    scale = scale + amount
  }

}


object PdfExportApp extends ScalaPAppCompanion {
}
