package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing.{Camera, Angles, ScalaPVector, ScalaPApplet}
import processing.event.MouseEvent

/**
 * Created on 2015/12/24.
 */
class GrabbingApp extends ScalaPApplet {

  import com.nekopiano.scala.processing.Angles._

  implicit val sp5 = this

  val cameraView = new Camera

  lazy val boxes =
    1 to 20 map(number =>{
      new Box(ScalaPVector(width/2.0f, height/2.0f, 400 + -25 * number))
    })

  var hoveredBoxes = Seq.empty[Box]
  var lockedBoxes = Seq.empty[Box]

  override def settings: Unit = {
    //size(1024, 768, P3D)
    //size(1024, 600, P3D)
    size(1280, 768, P3D)
    // the following can't work because here the real window size isn't defined yet.
    // size(width, height, P3D)

    // for Retina
    pixelDensity(2)
  }
  override def setup: Unit = {
    cameraView.initialize()
  }
  override def draw: Unit = {
    background(180)

    // init perspective projection based on new dimensions
    val cameraFOV = 60 * DEG_TO_RAD
    val cameraX = width / 2.0f
    val cameraY = height / 2.0f
    val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)
    val cameraNear = cameraZ / 10.0f
    val cameraFar = cameraZ * 10.0f
    val cameraAspect = width.toFloat / height.toFloat

    cameraView.camera()

    usingMatrix {
      translate(cameraView.eye)
      point(ScalaPVector.origin)
      text("eye", ScalaPVector.origin)
    }
    usingMatrix {
      translate(cameraView.center)
      point(ScalaPVector.origin)
      text("center", ScalaPVector.origin)
    }

    usingMatrix {
      translate(mouseX, mouseY, -mouseY)
      box(5)
    }

    val mousePoints = usingMatrix {
      val mouse = getMouseVector()

      // make the mouse position the rotating center
      translate(mouse)
      box(10)

      applyKeyPressedAngles()

      // 30 degrees on Y-axis
      val angleY = Angles.atan2(mouseY - height/2f, cameraView.eye.z)
      // TODO adjust x more
      val angleX = Angles.atan2(width/2f - mouseX, cameraView.eye.z)
      //val angleX = Angles.atan2((width/2f - mouseX) * sqrt((mouseY - height/2f) / (height/2f)), cameraView.eye.z)
      angles = Angles(angleX, angleY)

      rotateX(angles.y)
      text("angleY="+ angles.yDegrees + "°", ScalaPVector.origin.addY(-20))
      rotateY(angles.x)
      text("angleX="+ angles.xDegrees + "°", ScalaPVector.origin.addY(-40))


      text("mouse: x="+mouse.x + ", y="+ mouse.y, ScalaPVector.origin.addY(20))
      val screenMouse = screen(mouse)
      text("screenMouse: x="+screenMouse.x + ", y="+ screenMouse.y, ScalaPVector.origin.addY(40))
      val modelMouse = model(mouse)
      text("modelMouse: x="+modelMouse.x + ", y="+ modelMouse.y, ScalaPVector.origin.addY(60))

      line(ScalaPVector.origin, ScalaPVector.origin.setZ(-10000))
      val mouseRearOnScreen = screen(ScalaPVector.origin.setZ(-10000))
      val mouseRearOnModel = model(ScalaPVector.origin.setZ(-10000))
      (mouseRearOnScreen, mouseRearOnModel)
    }

    rect(0,0,100,100)

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
    lockedBoxes = Seq.empty[Box]
  }


}
object GrabbingApp {
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

case class Box(var vector: ScalaPVector)(implicit val sp5:ScalaPApplet) {

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
    val isWithinWidth = mouse.x >= screenOriginVector.x && mouse.x <= screenOriginVector.x + size
    val isWithinHeight = mouse.y >= screenOriginVector.y && mouse.y <= screenOriginVector.y + size
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
