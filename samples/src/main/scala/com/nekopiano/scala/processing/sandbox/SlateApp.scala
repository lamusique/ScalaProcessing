package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPApplet, ScalaPVector}
import processing.core.PApplet
import processing.core.PConstants._

/**
 * Created by neko on 2015/11/23.
 */
class SlateApp extends ScalaPApplet {

  import com.nekopiano.scala.processing.Angles._

  implicit val p5 = this
  val slates = new Slates()

  override def settings: Unit = {
    size(1024, 768, P3D)
  }
  override def setup: Unit = {
    // Here at the beginning you get the window size really
    slates.slates = Seq(new Slate(0,0,0), new Slate(350, 600, 500), new Slate(910, 200, 850))

    //camera(width/2.0f, height/2.0f, -500, width/2.0f, height/2.0f, 0, 0, 1, 0)
    //camera(width/2.0f, height/2.0f, (height/2.0f) / tan((PI/3f) / 2.0f), width/2.0f, height/2.0f, 0, 0, 1, 0)
    camera(width/2.0f, height/2.0f, (height/2.0f) / tan((PI/2f) / 2.0f), width/2.0f, height/2.0f, 0, 0, 1, 0)
  }
  override def draw: Unit = {
    background(180)


    //usingMatrix {
      mesh(-450)
    //}
    val z = mouseY - height
    text("z = "+ z, width/2,height - 50, 0)

    slates.move(0, 0, z)
    slates.display()

    val origin = ScalaPVector(0,0,0)

    val mouse = ScalaPVector(mouseX, mouseY, 0)
    slates.slates.foreach(slate=>{
      val position = slate.position
      dashedLine(origin, position)
      line(mouse, position)
      val lerped = mouse.lerp(position)
      text("angle = " + degrees(mouse.angleWith(position)) + ", slate=" + position, lerped.x, lerped.y)
    })

    text("O", 0,0,0)
    dashedLine(mouse, origin)
    val mouseRear = mouse.setZ(-100000)
    dashedLine(mouse, mouseRear)

    usingMatrix {
      //translate(width/2, height/2,0)
      val rate = (mouseX.toFloat - width/2f)/(width/2f)
      //rotateY(-radians(38.2f)*rate)
      rotateY(-PI/6*rate)
      dashedLine(mouse, mouseRear)
      text("rate="+rate, mouse.setY(100))
    }

    text("mouse="+mouse, mouse.x, mouse.y)

//    line(mouseX, mouseY, 0, mouseX, mouseY, -2000)
//    line(mouseX, mouseY, 0, mouseX + math.sqrt(z).toFloat, mouseY + math.sqrt(z).toFloat, -2000)
  }

  override def mousePressed: Unit = {
    slates.findHovered() match {
      case Some(slate) => slates.select(slate)
      case None => // do nothing
    }
  }
  override def mouseReleased: Unit = {
    slates.selected = None
  }

  def mesh(z:Int=0) = {
    val nbOfHorizontalLines = 10
    val nbOfVerticalLines = 10

    val distanceBetweenHorizontalLines = height/nbOfHorizontalLines
    val distanceBetweenVerticalLines = width/nbOfVerticalLines

    0 to distanceBetweenHorizontalLines foreach(i => {
      val yOfLine = i * nbOfHorizontalLines
      line(0, yOfLine, z, width, yOfLine, z)
    })
    0 to distanceBetweenVerticalLines foreach(i => {
      val yOfLine = i * nbOfVerticalLines
      line (yOfLine,0, z, yOfLine, height, z);
    })

  }
}

object SlateApp {
  val BOOTING_CLASS_NAME = this.getClass.getName.dropRight(1)
  def main(args: Array[String]) {
    // This specifies the class to be instantiated.
    val appletArgs = Array(BOOTING_CLASS_NAME)
    if (args != null) {
      PApplet.main(appletArgs ++ args)
    } else {
      PApplet.main(appletArgs)
    }
  }
}

class Slate(x:Int, y:Int, z:Int)(implicit val sp5:ScalaPApplet) {
  import sp5._

  val size = ScalaPVector(50, 10, 20)
  var position = ScalaPVector(x, y, z)

  def move(x:Int, y:Int, z:Int): Unit ={
    position = position.setZ(z)
  }

  def display(): Unit = {
    usingMatrix {
      camera()
      translate(position.x, position.y, position.z)
      box(size.x, size.y, size.z)
      val screenPosition = screen(position)
      text(s"position=$position, screen=$screenPosition", 0, size.y * 2, 0)
    }
  }

  def isHovered(mouseX:Int, mouseY:Int) = (mouseX > position.x - size.x && mouseX < position.x + size.x &&
      mouseY > position.y - size.y && mouseY < position.y + size.y)

}
class Slates()(implicit val sp5:ScalaPApplet) {
  import sp5._

  var selected: Option[Slate] = None

  // TODO val?
  var slates = {
    // TODO
    val z = mouseY - height
    Seq(new Slate(0,0,z), new Slate(width/8*3, height/8*3, z), new Slate(width/8*6, height/8*6, z))
  }

  def move(x:Int, y:Int, z:Int): Unit ={
    val z = mouseY - height
    slates.foreach(_.move(x, y, z))
  }
  def display(): Unit ={
    slates.foreach(_.display())
  }

  def findHovered() = {
    slates.find(_.isHovered(mouseX, mouseY))
  }

  def select(slate:Slate): Unit = {
    selected = Option(slate)
  }

}
