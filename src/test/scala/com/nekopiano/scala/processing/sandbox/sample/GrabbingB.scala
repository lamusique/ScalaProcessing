package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing._
import processing.core.PConstants
import processing.event.MouseEvent

/**
 * Created on 15/08/2016.
 */
class GrabbingBApp extends ThreeDimensionalCameraPApp {

  import com.nekopiano.scala.processing.Angles._

  implicit val sp5 = this

  val cameraView = new Camera

  lazy val boxes =
    1 to 200 map(number => {
      new ThreeDBox(ScalaPVector(width/2.0f, height/2.0f, 400 + -25 * number))
    })

  var hoveredBoxes = Seq.empty[ThreeDBox]
  var lockedBoxes = Seq.empty[ThreeDBox]

  override def settings: Unit = {
    size(1280, 768, P3D)

    // for Retina
    pixelDensity(displayDensity())

  }
  override def setup(): Unit = {
    lights()
  }

  override def drawObjects: Unit = {
    background(180)
    lights()

    usingMatrix {
      val mouse = getMouseVector()

      // make the mouse position the rotating center
      translate(mouse)

      // 30 degrees on Y-axis
      val angleY = Angles.atan2(mouseY - height/2f, cameraView.eye.z)
      // TODO adjust x more
      val angleX = Angles.atan2(width/2f - mouseX, cameraView.eye.z)
      //val angleX = Angles.atan2((width/2f - mouseX) * sqrt((mouseY - height/2f) / (height/2f)), cameraView.eye.z)
      angles = Angles(angleX, angleY)

      applyKeyPressedAngles()

      rotateX(angles.y)
      rotateY(angles.x)
    }

    val mouse = getMouseVector()

    hoveredBoxes = boxes.filter(_.isHover(mouse))

    val untouchedBoxes = boxes diff lockedBoxes
    untouchedBoxes foreach(_.move())

    untouchedBoxes foreach(_.display(color(255)))
    hoveredBoxes foreach(_.display(color(255, 0, 0)))
    lockedBoxes foreach(_.display(color(255, 100, 0)))

  }

  var angles = new Angles
  var angleEventSourcing = new Angles
  def applyKeyPressedAngles(): Unit = {
    if (angleEventSourcing != Angles.origin) {
      angles = angles.add(angleEventSourcing)
    }
  }

  override def keyPressed: Unit = {
    super.keyPressed()

    if (key == CODED) {
      angleEventSourcing = keyCode match {
        case UP => angleEventSourcing.addY(radians(0.5f))
        case DOWN => angleEventSourcing.addY(radians(-0.5f))
        case LEFT => angleEventSourcing.addX(radians(-0.5f))
        case RIGHT => angleEventSourcing.addX(radians(0.5f))
        case _ => angleEventSourcing // do nothing
      }
    }

  }
  override def keyReleased: Unit = {
    super.keyPressed()

    if (key == CODED) angleEventSourcing = Angles.origin
  }


  override def mousePressed(event:MouseEvent): Unit = {
    lockedBoxes = hoveredBoxes
    lockedBoxes.foreach(box => {
      box.offsetVector = ScalaPVector(mouseX - box.vector.x, mouseY - box.vector.y, box.vector.z)
    })
  }
  override def mouseDragged(event:MouseEvent): Unit = {
    lockedBoxes.foreach(box => {
      box.vector = box.vector.set(mouseX - box.offsetVector.x, mouseY - box.offsetVector.y)
    })
  }
  override def mouseReleased: Unit = {
    lockedBoxes = Seq.empty[ThreeDBox]
  }


}
object GrabbingBApp extends ScalaPAppCompanion {}

case class ThreeDBox(var vector: ScalaPVector)(implicit val sp5:ThreeDimensionalCameraPApp) {

  import sp5._

  val size = 20
  val easing = .0005f

  var targetVector = this.vector
  var offsetVector = this.vector

  def move(): Unit ={

    val targetMoveX = random(-50, 50)
    val targetMoveY = random(-50, 50)
    targetVector = targetVector.add(targetMoveX, targetMoveY, 0)
    if(targetVector.x > width || targetVector.x < 0) {
      targetVector = targetVector.addX(-targetMoveX)
    }
    if(targetVector.y > height || targetVector.y < 0) {
      targetVector = targetVector.addY(-targetMoveY)
    }


    val moveX = (targetVector.x - vector.x) * easing
    vector = vector.addX(moveX)

    val moveY = (targetVector.y - vector.y) * easing
    vector = vector.addY(moveY)

  }

  def isHover(mouse: ScalaPVector) = {
    val screenOriginVector = usingMatrix {
      translate(vector)
      screen(ScalaPVector.origin)
    }
    val halfSize = size / 2f
    val isWithinWidth = mouse.x >= screenOriginVector.x - size && mouse.x <= screenOriginVector.x + size
    val isWithinHeight = mouse.y >= screenOriginVector.y - size && mouse.y <= screenOriginVector.y + size
    isWithinWidth && isWithinHeight
  }

  def display(color:Int): Unit = {
    usingMatrix {
      translate(vector)
      usingStyle {
        fill(color)
        box(size)
      }
    }
  }

}
