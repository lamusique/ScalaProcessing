package com.nekopiano.scala.processing

import processing.event.{KeyEvent, MouseEvent}

/**
  * Created on 14/Sep/2016.
  */
trait CameraMixin extends ThreeDimensionalPApp {

  val lerpRatio = .075f
  lazy val classSimpleName = this.getClass.getSimpleName

  var lerpedTranslateVector = ScalaPVector.origin
  var targetTranslateVector = ScalaPVector.origin
  var lerpedRotateVector = ScalaPVector.origin
  var targetRotateVector = ScalaPVector.origin

  var isCommandPressed = false
  var isOptionPressed = false

  var record = false
  var printCount = 1

  abstract override def draw(): Unit = {
    if (record) {
      val countString = "%07d".format(printCount)
      beginRaw(PDF, classSimpleName + "-" + countString + ".pdf")
      //beginRecord(PDF, classSimpleName + "-" + countString + ".pdf")
    }

    lerpedTranslateVector = lerpedTranslateVector.lerp(targetTranslateVector, lerpRatio)
    lerpedRotateVector = lerpedRotateVector.lerp(targetRotateVector, lerpRatio)

    usingMatrix {

      translate(lerpedTranslateVector)
      rotateY(lerpedRotateVector.x / 100)
      //rotateZ(lerpedRotateVector.y / 100)
      rotateX(lerpedRotateVector.y / 100)

      super.draw()
    }
    superpose()

    if (record) {
      endRaw()
      //endRecord()
      record = false
    }
  }

  /**
    * Superpose after camera translation.
    */
  def superpose() {}

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

    val coefficient = 1.5f
    targetTranslateVector = targetTranslateVector.addZ(event.getCount * coefficient)

    val maxZ = 650f
    val minZ = -6500f
    targetTranslateVector.z match {
      case z:Float if z > maxZ => targetTranslateVector.setZ(maxZ)
      case z:Float if z < minZ => targetTranslateVector.setZ(minZ)
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
      val coefficient = .75f
      targetRotateVector = targetRotateVector.add(x * coefficient, y * coefficient, y * coefficient)
    }
  }

}
