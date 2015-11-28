package com.nekopiano.scala.processing.geometry

import com.nekopiano.scala.processing.{ScalaPConstants, ScalaPApplet}

/**
 * Created by neko on 2015/11/28.
 */
class PrismApp extends ScalaPApplet {
  implicit val sp5 = this

  override def settings: Unit = {
      size(800, 600, P3D);
  }
  override def setup: Unit = {

  }
  override def draw: Unit = {

    background(200)

    val prism = new TriangularPrism()

    //fill(100,100)

    pushMatrix()

    translate(mouseX, mouseY, -mouseY)
    rotateX(PI/360 * mouseY)
    rotateY(PI/360 * mouseX)
    prism.display(100,60)

    popMatrix()

  }

}
object PrismApp {
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
class TriangularPrism(implicit val sp5:ScalaPApplet) {
  import sp5._

  def display(size:Int):Unit = display(size, size)
  def display(size:Int, depth:Int) {

    // double triangles
    displayTriangle(size)

    usingMatrix {
      translate(0,0,-depth)
      displayTriangle(size)
    }

    // sides
    val halfSize = size/2

    // right side
    usingMatrix {
      rotateY(PI/2)
      rotateX(PI/6)
      rect(0,0,depth,size)
    }

    // left side
    usingMatrix {
      rotateY(-PI/2)
      rotateX(PI/6)
      rect(-depth,0,depth,size)
    }

    val triangleHeight = size * sin(PI/3)
    // bottom side
    usingMatrix {
      rotateX(PI/2)
      translate(0,0,-triangleHeight)
      rect(-halfSize,-depth,size,depth)
    }

  }

  def displayTriangle(size:Int): Unit = {
    val halfSize = size/2
    val triangleHeight = size * sin(PI/3)
    // the left bottom
    val lbX = -halfSize
    val lbY = triangleHeight
    // the right bottom
    val rbX = halfSize
    val rbY = triangleHeight

    triangle(0, 0, lbX, lbY, rbX, rbY);
  }
}

