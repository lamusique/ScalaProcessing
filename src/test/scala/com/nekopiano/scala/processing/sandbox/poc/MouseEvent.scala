package com.nekopiano.scala.processing.sandbox.poc

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPVector, ThreeDimensionalPApp}
import processing.event.MouseEvent;

/**
 * Created on 27/07/2016.
 */
class MouseEventApp extends ThreeDimensionalPApp {

  var translateVector = ScalaPVector.origin
  var record = false

  override def settings(): Unit = {
    size(800, 600, P3D)

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
      beginRaw(PDF, "orgchart-output.pdf");
      //beginRecord(PDF, "output.pdf");
    }



    translate(translateVector)

    background(256)

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
    text("translateVector=" + translateVector, ScalaPVector(width/2, height/2))

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


  override def mouseWheel(event:MouseEvent) {

    print("event.getCount=" + event.getCount)
    print(", ")
//    print("event.getX=" + event.getX)
//    print(", ")
//    print("event.getY=" + event.getY)
//    print(", ")
//    print("event.getButton=" + event.getButton)
//    print(", ")
//    print("event.getAction=" + event.getAction)
//    print(", ")
//    print("event.getFlavor=" + event.getFlavor)
//    print(", ")
    val native = event.getNative
    val castNative = native match {
      case macMouseEvent: com.jogamp.newt.event.MouseEvent => {
        val rotation = macMouseEvent.getRotation

        // negative left, positive right.
        val amountX = rotation(0)
        print("amountX=" + amountX)
        print(", ")
        translateVector = translateVector.addX(-amountX)

        // negative up, positive down.
        val amountY = rotation(1)
        print("amountY=" + amountY)
        print(", ")
        translateVector = translateVector.addZ(-amountY)
      }
      case _ => native
    }
    castNative
    println()

    println("translateVector=" + translateVector)

    print("event.getNative=" + event.getNative)
//    print(", ")
//    print("event.toString=" + event.toString)
//    print(", ")
//    print("mouseX=" + mouseX)
//    print(", ")
//    print("mouseY=" + mouseY)
//    print(", ")
//    print("pmouseX=" + pmouseX)
//    print(", ")
//    print("pmouseY=" + pmouseY)
//    print(", ")
    println()
  }

}


object MouseEventApp extends ScalaPAppCompanion {
}
