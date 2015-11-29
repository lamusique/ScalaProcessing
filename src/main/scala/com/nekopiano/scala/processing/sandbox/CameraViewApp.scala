package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPVector, ScalaPApplet}

/**
 * Created by neko on 2015/11/29.
 */
class CameraViewApp extends ScalaPApplet {

  override def settings: Unit = {
    size(1024, 768, P3D)
    // the following can't work because here the real window size isn't defined yet.
    // size(width, height, P3D)
  }
  override def setup: Unit = {
  }
  override def draw: Unit = {
    background(225)

    //camera(width/2.0f, height/2.0f, (height/2.0f) / tan((PI/3f) / 2.0f), width/2.0f, height/2.0f, 0, 0, 1, 0)
    //camera(width/2.0f, height/2.0f, mouseY, width/2.0f, height/2.0f, 0, 0, 1, 0)

    val eye = ScalaPVector(width/2.0f,height/2.0f,0)
    val center = ScalaPVector(width/2.0f,height/2.0f,-500)
    //camera(eye, center)

    // init perspective projection based on new dimensions
    val cameraFOV = 60 * DEG_TO_RAD
    val cameraX = width / 2.0f
    val cameraY = height / 2.0f
    val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)
    val cameraNear = cameraZ / 10.0f
    val cameraFar = cameraZ * 10.0f
    val cameraAspect = width.toFloat / height.toFloat

    //perspective(PI/3f, width/height, cameraNear, cameraFar)

    usingMatrix {
      translate(eye)
      point(ScalaPVector.origin)
      text("eye", ScalaPVector.origin)
    }
    usingMatrix {
      translate(center)
      point(ScalaPVector.origin)
      text("center", ScalaPVector.origin)
    }

    usingMatrix {
      translate(mouseX, mouseY, -mouseY)
      box(5)
    }

    usingMatrix {
      val mouse = getMouseVector()
      val rearMouse = mouse.setZ(-8000)
      val angleY = (PI/3/2) * (mouseY - height/2f)/(height/2f)
      //rotateY(PI/3/2)
      rotateX(angleY)
      val angleX = (PI/3/2) * (width/2f - mouseX)/(width/2f)
      rotateY(angleX)
      text("angleX="+angleX,mouse)
      line(mouse, rearMouse)
    }

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
