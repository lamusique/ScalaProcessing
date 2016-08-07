package com.nekopiano.scala.processing

import processing.event.{KeyEvent, MouseEvent}

/**
  * Created on 07/08/2016.
  */
trait ThreeDimensionalCameraPApp extends ThreeDimensionalPApp {

  var lerpedTranslateVector = ScalaPVector.origin
  var targetTranslateVector = ScalaPVector.origin
  var lerpedRotateVector = ScalaPVector.origin
  var targetRotateVector = ScalaPVector.origin


  var isCommandPressed = false
  var isOptionPressed = false

  var record = false
  var printCount = 1
  lazy val classSimpleName = this.getClass.getSimpleName

  override def draw(): Unit = {
    if (record) {
      val countString = "%07d".format(printCount)
      beginRaw(PDF, classSimpleName + "-" + countString + ".pdf")
      //beginRecord(PDF, classSimpleName + "-" + countString + ".pdf")
    }

    lerpedTranslateVector = lerpedTranslateVector.lerp(targetTranslateVector, .075f)
    lerpedRotateVector = lerpedRotateVector.lerp(targetRotateVector, .075f)

    usingMatrix {

      translate(lerpedTranslateVector)
      rotateY(lerpedRotateVector.x / 100)
      rotateZ(lerpedRotateVector.y / 100)

      drawObjects()
    }
    drawFixedObjects()

    if (record) {
      endRaw()
      //endRecord()
      record = false
    }
  }

  def drawObjects()
  def drawFixedObjects() {}

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
    targetTranslateVector = targetTranslateVector.addZ(event.getCount)
    targetTranslateVector.z match {
      case z:Float if z > 650f => targetTranslateVector.setZ(650f)
      case z:Float if z < -6500f => targetTranslateVector.setZ(-6500f)
      case _ => // do nothing
    }
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
