package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing._
import processing.event.MouseEvent

/**
 * Created by neko on 2015/11/29.
 */
class CameraViewApp extends ScalaPApplet {
//  class CameraViewApp extends ThreeDimensionalCameraPApp {

  import com.nekopiano.scala.processing.Angles._

  implicit val sp5 = this

  val cameraView = new ScalaPAppletCamera

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

    pixelDensity(2)
  }
  override def setup: Unit = {
    cameraView.initialize()
  }
  override def draw: Unit = {
    background(180)

    //camera(width/2.0f, height/2.0f, (height/2.0f) / tan((PI/3f) / 2.0f), width/2.0f, height/2.0f, 0, 0, 1, 0)
    //camera(width/2.0f, height/2.0f, mouseY, width/2.0f, height/2.0f, 0, 0, 1, 0)

    // init perspective projection based on new dimensions
    val cameraFOV = 60 * DEG_TO_RAD
    val cameraX = width / 2.0f
    val cameraY = height / 2.0f
    val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)
    val cameraNear = cameraZ / 10.0f
    val cameraFar = cameraZ * 10.0f
    val cameraAspect = width.toFloat / height.toFloat

    // by default
    //val eye = ScalaPVector(width/2.0f, height/2.0f, cameraZ)
    //val center = ScalaPVector(width/2.0f, height/2.0f, 0)

    //val eye = ScalaPVector(width/2.0f, height/2.0f, 0)
    //val center = ScalaPVector(width/2.0f, height/2.0f, 0)

    //camera(eye, center)

    //camera(ScalaPVector(200, 100, 400), center)
    //perspective(PI/3f, width/height, cameraNear, cameraFar)


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


    text("angle=" + Angles.degrees(Angles.atan2(height/2-mouseY, cameraView.eye.z)), width/3, height - 110)
    text("mouse=" + mouse, width/3, height - 95)
    text("mouseRearOnScreen=" + mousePoints._1, width/3, height - 80)
    text("mouseRearOnModel=" + mousePoints._2, width/3, height - 65)
    text("cameraView=" + cameraView, width/3, height - 50)
    text("angles=" + angles, width/3, height - 35)
    text("angleEventSourcing=" + angleEventSourcing, width/3, height - 20)

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

//    if(vector.x > width || vector.x < 0) {
//      vector = vector.addX(-moveX)
//    }
//    if(vector.y > height || vector.y < 0) {
//      vector = vector.addY(-moveY)
//    }
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
    val vectors = usingMatrix {
      translate(vector)
      usingStyle {
        fill(color)
        box(size)
      }

//      text("vector: x="+vector.x + ", y="+ vector.y, ScalaPVector.origin.addY(20))
//      val screenVector = screen(vector)
//      text("screenVector: x="+screenVector.x + ", y="+ screenVector.y, ScalaPVector.origin.addY(40))
//      val modelVector = model(vector)
//      text("modelVector: x="+modelVector.x + ", y="+ modelVector.y, ScalaPVector.origin.addY(60))
      val screenOriginVector = screen(ScalaPVector.origin)
      text("screenOriginVector: x="+screenOriginVector.x + ", y="+ screenOriginVector.y, ScalaPVector.origin.addY(80))

//      text("x=" + screenVector.x.toString,ScalaPVector.origin.addY(20))
//      text("y=" + screenVector.y.toString,ScalaPVector.origin.addY(40))
//      text("z=" + screenVector.z.toString,ScalaPVector.origin.addY(60))

      (vector, screenOriginVector)
    }
    line(vectors._1, vectors._2.addZ(100))
  }

}
