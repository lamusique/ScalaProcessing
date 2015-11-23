package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPVector, ScalaPApplet}
import processing.core.{PConstants, PApplet}

import scala.util.Random

/**
 * Created by neko on 2015/11/23.
 */
class MacrosExplosionApp extends ScalaPApplet {

  implicit val sPApplet = this
  val rand = new Random()

  override def settings: Unit = {
    size(1024, 650, PConstants.P2D)
  }
  override def setup: Unit = {
  }

  val NUMBER_OF_EXPLOSIONS = 500
  val NUMBER_OF_SIMULTANEOUS_EXPLOSIONS = 25

  var explosions = Set.empty[Explosion]
  override def draw: Unit = {
    background(0)

    if (explosions.size < NUMBER_OF_EXPLOSIONS) {
      val number = random(NUMBER_OF_SIMULTANEOUS_EXPLOSIONS)
      val appendingExplosions = (0 to random(NUMBER_OF_SIMULTANEOUS_EXPLOSIONS).toInt).filter(_ > 0).map(_ => Explosion())
      explosions = explosions ++ appendingExplosions
    }

    //explosion.
    explosions = explosions.filterNot(_.isVanished())
    explosions.foreach(_.explode())

    fill(150)
    text("The number of explosions = " + explosions.size, width/3, height - 20)
  }

}
object MacrosExplosionApp {
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

class Explosion(val x:Float, val y:Float, val size:Float = 0, var delayFrames:Int = 0)(implicit val sPApplet: ScalaPApplet) {

  import sPApplet._

  val vector = ScalaPVector(x, y)
  val SHIFT_RANGE = size/4
  val vanishingPoint = ScalaPVector(random(x - SHIFT_RANGE, x + SHIFT_RANGE), random(y - SHIFT_RANGE, y + SHIFT_RANGE))
  val COLOR = color(255, 204, 0)

  var alpha = 0.1f
  var currentSize = 0f
  var vanishingSize = 0f

    def explode(): Unit = {
      if(isVanished()) return

      alpha = alpha + 16f
      noStroke()
      fill(COLOR, alpha)
      if (currentSize < size)
        currentSize = currentSize + 6f
      ellipse(x, y, currentSize, currentSize)

      if (currentSize > size/4) vanish()
    }

  private def vanish(): Unit = {
    fill(0)
    vanishingSize = vanishingSize + 4f
    ellipse(vanishingPoint.x, vanishingPoint.y, vanishingSize, vanishingSize)
  }

  def isVanished() = vanishingSize > Explosion.SIZE * 1.5

}
object Explosion {
  val SIZE = 50
  def apply()(implicit sPApplet: ScalaPApplet) = {
    import sPApplet._
    new Explosion(random(width), random(height), random(SIZE), random(1000).toInt)
  }
}
