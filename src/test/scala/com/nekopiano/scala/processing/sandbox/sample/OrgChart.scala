package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPVector, ThreeDimensionalPApp, TwoDimensionalPApp}
import processing.event.{KeyEvent, MouseEvent};

/**
  * Created on 27/07/2016.
  */
class OrgChartApp extends ThreeDimensionalPApp {

  var lerpedTranslateVector = ScalaPVector.origin
  var targetTranslateVector = ScalaPVector.origin
  var lerpedRotateVector = ScalaPVector.origin
  var targetRotateVector = ScalaPVector.origin

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
    //background(250)

  }

  var startX = 0

  override def draw(): Unit = {

    if (record) {
      beginRaw(PDF, "orgchart-output.pdf");
      //beginRecord(PDF, "output.pdf");
    }

    lerpedTranslateVector = lerpedTranslateVector.lerp(targetTranslateVector, .075f)
    lerpedRotateVector = lerpedRotateVector.lerp(targetRotateVector, .075f)

    usingMatrix {

      translate(lerpedTranslateVector)
      rotateY(lerpedRotateVector.x / 100)
      rotateZ(lerpedRotateVector.y / 100)

      background(255)

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

      fill(150, 50)
      box(100)

    }

    usingMatrix {

      stroke(random(50), random(255), random(255), 100)
      line(startX, 0, random(0, width), height)
      if (startX < width) {
        startX += 1
      } else {
        startX = 0
      }

    }

    usingMatrix {
      fill(255,0,0);
      ellipse(100,100,95,95);
      //filter(BLUR, 6)
      stroke(0);
      fill(255,255,0);
      ellipse(100,100,90,90);
    }

    textSize(8)
    fill(10)
    text("lerpedTranslateVector=" + lerpedTranslateVector, ScalaPVector(10, height - 20))
    text("targetTranslateVector=" + targetTranslateVector, ScalaPVector(10, height - 10))
    text("lerpedRotateVector=" + lerpedRotateVector, ScalaPVector(300, height - 20))
    text("targetRotateVector=" + targetRotateVector, ScalaPVector(300, height - 10))

    //    translate(width/2, height/2, -200)
    //    rotateZ(0.2F)
    //    rotateY(mouseX/500.0F)
    //    box(100)


    //exit()
    //endRecord()
    if (record) {
      //endRaw()
      endRecord()
      record = false
    }

    // Error: super may not be used on variable keyPressed
    //println("keyPressed=" + keyPressed)

  }


  var isCommandPressed = false
  var isOptionPressed = false

  // Hit 'r' to record a single frame
  override def keyPressed() {

    if (key == 'r') {
      record = true
    }

    if (key == CODED) {
      keyCode match {
        case 157 => isCommandPressed = true
        case ALT => isOptionPressed = true
        case _ => // do nothing
      }
    }

  }

  override def keyReleased(event: KeyEvent) {
    if (key == CODED) {
      keyCode match {
        case 157 => isCommandPressed = false
        case ALT => isOptionPressed = false
        case _ => // do nothing
      }
    }
  }

  override def mouseWheel(event: MouseEvent) {

    //println("event.getCount=" + event.getCount)
    targetTranslateVector = targetTranslateVector.addZ(event.getCount)

    //println("translateVector=" + translateVector)

  }

  override def mouseMoved(event: MouseEvent): Unit = {

    val x = pmouseX * (mouseX - pmouseX) /100
    val y = pmouseY * (mouseY - pmouseY) /100

    if (isCommandPressed) {
      targetTranslateVector = targetTranslateVector.add(x, y, 0)
    }
    if (isOptionPressed) {
      targetRotateVector = targetRotateVector.add(x, y, 0)
    }
  }

}


object OrgChartApp extends ScalaPAppCompanion {

}

