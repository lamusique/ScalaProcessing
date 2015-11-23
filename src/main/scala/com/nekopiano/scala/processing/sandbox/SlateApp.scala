package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPVector, ScalaPApplet}
import processing.core.PConstants._

/**
 * Created by neko on 2015/11/23.
 */
class SlateApp extends ScalaPApplet {

  implicit val p5 = this
  val slates = new Slates()

  override def settings: Unit = {
    size(1024, 768, P3D)
  }
  override def setup: Unit = {

  }
  override def draw: Unit = {
    background(200)
    val z = mouseY - height
    text("z = "+ z, width/2,height - 50, 0)

    slates.move(0,0,z)
    slates.display()
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

}

object SlateApp {
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

class Slate(var x:Int, var y:Int, var z:Int)(implicit val p5:ScalaPApplet) {
  import p5._

  val position = new ScalaPVector(x, y, z)
  val size = new ScalaPVector(50, 10, 20)

  def display(): Unit = {
    translate(x, y, z)
    box(size)
  }

  def isHovered(mouseX:Int, mouseY:Int) = (mouseX > position.x - size.x && mouseX < position.x + size.x &&
      mouseY > position.y - size.y && mouseY < position.y + size.y)

}
class Slates()(implicit val p5:ScalaPApplet) {
  import p5._

  var selected: Option[Slate] = None

  val slates = {
    val z = mouseY - height
    Seq(new Slate(0,0,z), new Slate(width/8*3, height/8*3, z), new Slate(width/8*6, height/8*6, z))
  }

  def move(x:Int, y:Int, z:Int): Unit ={
    val z = mouseY - height
    slates.foreach(_.z = z)
  }
  def display(): Unit ={
    slates.foreach(_.display())
    //pushMatrix()
    //fill(123)
    //selected.foreach(_.display())
    //popMatrix()
  }

  def findHovered() = {
    slates.find(_.isHovered(mouseX, mouseY))
  }

  def select(slate:Slate): Unit = {
    selected = Option(slate)
  }

}
