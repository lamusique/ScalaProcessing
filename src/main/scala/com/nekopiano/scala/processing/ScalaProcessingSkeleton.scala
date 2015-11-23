package com.nekopiano.scala.processing

import processing.core.{PConstants, PApplet}

/**
 * Created by neko on 2015/11/23.
 */
class ScalaProcessingSkeleton extends PApplet {

  override def settings: Unit = {
    size(1024, 768, PConstants.P3D)
  }
  override def setup: Unit = {

  }
  override def draw: Unit = {
    // TODO: snippet codes
    background(200)
    val z = mouseY - height
    text("z = "+ z, width/2,height - 50, 0)
    translate(0, 0, z)
    box(50)
    translate(width/2, height/2, z)
    box(50)
    translate(width/3, height/3, z)
    box(50)
  }

}
object ScalaProcessingSkeleton {
  val BOOTING_CLASS_NAME = this.getClass.getName.dropRight(1)
  def main(args: Array[String]) {
    // This specifies the class to be instantiated.
    val appletArgs = Array(BOOTING_CLASS_NAME)
    if (args != null) {
      PApplet.main(PApplet.concat(appletArgs, args))
    } else {
      PApplet.main(appletArgs)
    }
  }
}
