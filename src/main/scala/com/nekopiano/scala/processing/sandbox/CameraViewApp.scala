package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{Camera, Angles, ScalaPVector, ScalaPApplet}

/**
 * Created by neko on 2015/11/29.
 */
class CameraViewApp extends ScalaPApplet {

  import com.nekopiano.scala.processing.Angles._

  implicit val sp5 = this

  val cameraView = new Camera

  override def settings: Unit = {
    //size(1024, 768, P3D)
    size(1024, 600, P3D)
    // the following can't work because here the real window size isn't defined yet.
    // size(width, height, P3D)
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
      //val rearMouse = mouse.setZ(-8000)

      // make the mouse position the rotating center
      translate(mouse)

      applyKeyPressedAngles()

      // 30 degrees
      val baseAngle = PI/6
      // TODO use tangent
      //val angleStep = baseAngle/(height/2)

//      val angleX = baseAngle * (width/2f - mouseX)/(width/2f)
//      val angleY = baseAngle * (mouseY - height/2f)/(height/2f)

//      val angleX = angleStep * (width/2f - mouseX)
//      val angleY = angleStep * (mouseY - height/2f)
//      angles = Angles(angleX, angleY)

      rotateX(angles.y)
      //text("angleY="+ degrees(angleY) + "°", mouse.addY(20))
      text("angleY="+ angles.yDegrees + "°", ScalaPVector.origin.addY(20))

      //val angleX = baseAngle * (width/2f - mouseX)/(width/2f)
      rotateY(angles.x)
      //text("angleX="+ degrees(angleX) + "°", mouse.addY(-20))
      text("angleX="+ angles.xDegrees + "°", ScalaPVector.origin.addY(-20))

      //line(mouse, rearMouse)
      line(ScalaPVector.origin, ScalaPVector.origin.setZ(-10000))
      val mouseRearOnScreen = screen(ScalaPVector.origin.setZ(-10000))
      val mouseRearOnModel = model(ScalaPVector.origin.setZ(-10000))
      (mouseRearOnScreen, mouseRearOnModel)
    }

    rect(0,0,100,100)

    val mouse = getMouseVector()
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
