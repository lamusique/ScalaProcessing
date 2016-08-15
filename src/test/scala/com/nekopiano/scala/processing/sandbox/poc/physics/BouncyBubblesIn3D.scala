package com.nekopiano.scala.processing.sandbox.poc.physics

import com.nekopiano.scala.processing._

/**
  * Created on 09/08/2016.
  */
class BouncyBubblesIn3DApp extends ThreeDimensionalCameraPApp {

  implicit val sp5 = this

  val numBalls = 48
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

    usingMatrix {
      val gravityVector = ScalaPVector(width/2, height, 0)
      val heavenVector = ScalaPVector(width/2, 0, 0)
      usingStyle {
        stroke(222, 111, 123, 222)
        line(heavenVector, gravityVector)
        textSize(36)
        text("G", gravityVector)
      }
    }
  }
}

object BouncyBubblesIn3DApp extends ScalaPAppCompanion {}


class Ball(var vector: ScalaPVector, val diameter: Float, val id: Int)(implicit val sp53d: ThreeDimensionalPApp) {

  import sp53d._

  val spring = 0.03f
  val gravity = 0.1f
  val friction = -0.2f

  var velocity = ScalaPVector.origin

  var others: Set[Ball] = Set.empty[Ball]

  def collide() {

    others.foreach(ball => {
      val dx = ball.vector.x - vector.x
      val dy = ball.vector.y - vector.y
      val dz = ball.vector.z - vector.z

      //val distance = sqrt(dx * dx + dy * dy)
      val distance = sqrt(dx * dx + dy * dy + dz * dz)
      val minDist = ball.diameter / 2 + this.diameter / 2

      if (distance < minDist) {
        import com.nekopiano.scala.processing.Angles._

        val angle = atan2(dy, dx)
        val angleZX = atan2(dx, dz)

        val targetX = vector.x + cos(angle) * minDist;
        val targetY = vector.y + sin(angle) * minDist

        val targetZ = vector.z + cos(angleZX) * minDist

        val ax = (targetX - ball.vector.x) * spring;
        val ay = (targetY - ball.vector.y) * spring
        val az = (targetZ - ball.vector.z) * spring

        velocity = velocity.add(-ax, -ay, -az)

        ball.velocity = ball.velocity.add(ax, ay, az)
      }

    })
  }

  def move() {
    velocity = velocity.addY(gravity)
    vector = vector.add(velocity)

    val radius = diameter / 2
    val frictionX =
      if (vector.x + radius > width) {
        vector = vector.setX(width - radius)
        friction
      }
      else if (vector.x - radius < 0) {
        vector = vector.setX(radius)
        friction
      } else {
        1
      }

    val frictionY =
      if (vector.y + radius > height) {
        vector = vector.setY(height - radius)
        friction
      }
      else if (vector.y - radius < 0) {
        vector = vector.setY(radius)
        friction
      } else {
        1
      }

    val frictionZ =
      if (vector.z + radius > width) {
        vector = vector.setZ(width - radius)
        friction
      }
      else if (vector.z - radius < 0) {
        vector = vector.setZ(radius)
        friction
      } else {
        1
      }

    val frictionVector = ScalaPVector(frictionX, frictionY, frictionZ)

    velocity = velocity.multiply(frictionVector)

  }

  def display() {
    usingMatrix {
      translate(vector)
      sphere(diameter / 2)
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
