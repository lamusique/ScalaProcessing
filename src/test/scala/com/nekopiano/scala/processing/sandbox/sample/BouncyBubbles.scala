package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing.ScalaPApplet
import processing.core.PApplet

/**
 * Ported from https://processing.org/examples/bouncybubbles.html
 * Created on 2015/11/23.
 */
class BouncyBubbles extends ScalaPApplet {
  import BouncyBubbles._

  val balls:Set[Ball] = {
    // in parallel
    val localBalls = (0 to numBalls).par.map(i => {
      new Ball(random(width), random(height), random(30, 70), i)
    }).seq.toSet
    localBalls.foreach(_.others = localBalls)
    localBalls
  }

  override def settings(): Unit = {
    size(640, 360)
  }

  override def setup() {
    noStroke()
    fill(255, 204)
  }

  override def draw() {
    background(0)
    balls.foreach(ball => {
      ball.collide()
      ball.move()
      ball.display()
    })
  }

}

object BouncyBubbles {
  // Constants
  val numBalls = 12

  val spring = 0.05f
  val gravity = 0.03f
  val friction = -0.9f

  // ==== booting process
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

class Ball(var x:Float, var y:Float, val diameter:Float, val id:Int)(implicit val sp5: ScalaPApplet) {

  import sp5._
  import com.nekopiano.scala.processing.Angles._
  import BouncyBubbles._

  var others: Set[Ball] = Set.empty[Ball]

  var vx = 0f
  var vy = 0f

  def collide() {
    others.foreach(ball => {
      val dx = ball.x - x
      val dy = ball.y - y
      val distance = sqrt(dx*dx + dy*dy)
      val minDist = ball.diameter/2 + this.diameter/2
      if (distance < minDist) {
        val angle = atan2(dy, dx)
        val targetX = x + cos(angle) * minDist
        val targetY = y + sin(angle) * minDist
        val ax = (targetX - ball.x) * spring
        val ay = (targetY - ball.y) * spring
        vx -= ax
        vy -= ay
        ball.vx += ax
        ball.vy += ay
      }

    })
  }

  def move() {
    vy += gravity
    x += vx
    y += vy
    if (x + diameter/2 > width) {
      x = width - diameter/2
      vx *= friction
    }
    else if (x - diameter/2 < 0) {
      x = diameter/2
      vx *= friction
    }
    if (y + diameter/2 > height) {
      y = height - diameter/2
      vy *= friction
    }
    else if (y - diameter/2 < 0) {
      y = diameter/2
      vy *= friction
    }
  }

  def display() {
    ellipse(x, y, diameter, diameter)
  }
}
