package com.nekopiano.scala.processing.sandbox.poc.physics

import com.nekopiano.scala.processing._

/**
  * Created on 09/08/2016.
  */
class BouncyBubblesIn3DApp extends ThreeDimensionalCameraPApp {

  implicit val sp5 = this

  val numBalls = 12
  var balls: Set[Ball] = null

  override def settings: Unit = {
    size(1024, 768, P3D)
    pixelDensity(displayDensity())
    //smooth(8)
  }

  override def setup(): Unit = {
    surface.setResizable(true)
    colorMode(HSB, 360, 100, 100, 100)

    noStroke()

    val localBalls = (0 to numBalls).map(i => {
      //new Ball(random(width), random(height), random(30, 70), i)
      Ball(i)
    }).toSet
    localBalls.foreach(_.others = localBalls)
    balls = localBalls
  }

  override def drawObjects(): Unit = {
    background(0, 0, 0)

    lights()

    fill(255, 204, 200, 100)


    balls.foreach(ball => {
      ball.collide();
      ball.move();
      ball.display();
    })

  }
}

object BouncyBubblesIn3DApp extends ScalaPAppCompanion {}


//class Ball(var x: Float, var y: Float, val diameter: Float, val id: Int)(implicit val sp5: ScalaPApp) {
class Ball(var vector: ScalaPVector, val diameter: Float, val id: Int)(implicit val sp53d: ThreeDimensionalPApp) {

  import sp53d._

  val spring = 0.05f;
  val gravity = 0.03f;
  val friction = -0.9f;


  var others: Set[Ball] = Set.empty[Ball]

  var vx = 0f
  var vy = 0f

  def collide() {

    others.foreach(ball => {
      val dx = ball.vector.x - vector.x
      val dy = ball.vector.y - vector.y
      val distance = sqrt(dx * dx + dy * dy);
      val minDist = ball.diameter / 2 + this.diameter / 2;

      if (distance < minDist) {
        import com.nekopiano.scala.processing.Angles._

        val angle = atan2(dy, dx);
        val targetX = vector.x + cos(angle) * minDist;
        val targetY = vector.y + sin(angle) * minDist;
        val ax = (targetX - ball.vector.x) * spring;
        val ay = (targetY - ball.vector.y) * spring;
        vx -= ax
        vy -= ay
        ball.vx += ax
        ball.vy += ay
      }

    })
  }

  def move() {
    vy += gravity
    vector = vector.add(vx, vy, 0)
    if (vector.x + diameter / 2 > width) {
      vector = vector.setX(width - diameter / 2)
      vx *= friction
    }
    else if (vector.x - diameter / 2 < 0) {
      vector = vector.setX(diameter / 2)
      vx *= friction
    }
    if (vector.y + diameter / 2 > height) {
      vector = vector.setY(height - diameter / 2)
      vy *= friction;
    }
    else if (vector.y - diameter / 2 < 0) {
      vector = vector.setY(diameter / 2)
      vy *= friction
    }
  }

  def display() {
    usingMatrix {
      translate(vector)
      sphere(diameter/2)
    }
  }
}
object Ball {
  def apply(id: Int)(implicit sp53d: ThreeDimensionalPApp): Ball = {
    import sp53d._
    val vector = ScalaPVector(random(width), random(height), 0)
    new Ball(vector, random(30, 70), id)
  }
}
