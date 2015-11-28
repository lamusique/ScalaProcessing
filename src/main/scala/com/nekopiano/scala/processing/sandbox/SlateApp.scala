package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPApplet, ScalaPVector}
import processing.core.PApplet
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
    // Here at the beginning you get the window size really
    val z = mouseY - height
    slates.slates = Seq(new Slate(0,0,z), new Slate(width/8*3, height/8*3, z), new Slate(width/8*6, height/8*6, z))

  }
  override def draw: Unit = {
    background(180)
    val z = mouseY - height
    text("z = "+ z, width/2,height - 50, 0)

    slates.move(0, 0, z)
    slates.display()

    val mouse = ScalaPVector(mouseX, mouseY, 0)
    val sample = slates.slates(1).position
    line(mouse, sample)
    text("angle = " + mouse.angleWith(sample) + sample + mouse, width/2,height - 50, 50)

    line(mouseX, mouseY, 0, mouseX, mouseY, -2000)
    line(mouseX, mouseY, 0, mouseX + math.sqrt(z).toFloat, mouseY + math.sqrt(z).toFloat, -2000)
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
      PApplet.main(appletArgs ++ args)
    } else {
      PApplet.main(appletArgs)
    }
  }
}

class Slate(x:Int, y:Int, z:Int)(implicit val p5:PApplet) {
  import p5._

  val position = ScalaPVector(x, y, z)
  val size = ScalaPVector(50, 10, 20)

  def move(x:Int, y:Int, z:Int): Unit ={
    position.z = z
  }

  def display(): Unit = {
    pushMatrix()
    translate(position.x, position.y, position.z)
    box(size.x, size.y, size.z)
    text(s"$position", 0, size.y * 2, 0)
    popMatrix()
  }

  def isHovered(mouseX:Int, mouseY:Int) = (mouseX > position.x - size.x && mouseX < position.x + size.x &&
      mouseY > position.y - size.y && mouseY < position.y + size.y)

}
class Slates()(implicit val p5:PApplet) {
  import p5._

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
