package com.nekopiano.scala.processing.sandbox.poc.physics

import com.nekopiano.scala.processing._
import com.typesafe.scalalogging.LazyLogging

/**
  * Created on 09/08/2016.
  */
class BouncyBubblesIn3DWSoundApp extends ThreeDimensionalCameraPApp with LazyLogging {

  implicit val sp5 = this

  val numBalls = 32
  var balls: Set[SoundBall] = null

  override def settings: Unit = {
    size(1024, 768, P3D)
    pixelDensity(displayDensity())
    //smooth(8)

    SuperColliderSynth.runServer()
  }

  override def setup(): Unit = {
    surface.setResizable(true)
    colorMode(HSB, 360, 100, 100, 100)

    noStroke()

    val localBalls = (0 until numBalls).map(i => {
      SoundBall(i)
    }).toSet
    localBalls.foreach(ball => {ball.others = localBalls - ball})
    balls = localBalls

    logger.info("waiting")
    Thread.sleep(5000)
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

object BouncyBubblesIn3DWSoundApp extends ScalaPAppCompanion {}


class SoundBall(var vector: ScalaPVector, val diameter: Float, val id: Int)(implicit val sp53d: ThreeDimensionalPApp) extends LazyLogging {

  import sp53d._

//  val spring = 0.05f
//  val gravity = 0.03f
//  val friction = -0.9f
  val spring = 0.06f
  val gravity = 0.05f
  val friction = -0.4f

  var velocity = ScalaPVector.origin

  var others: Set[SoundBall] = Set.empty[SoundBall]

  def collide() {

    others.foreach(ball => {
      val dx = ball.vector.x - vector.x
      val dy = ball.vector.y - vector.y
      val dz = ball.vector.z - vector.z

      val distance = sqrt(dx * dx + dy * dy + dz * dz)
      val minDist = ball.diameter / 2 + this.diameter / 2

      if (distance < minDist) {
        import com.nekopiano.scala.processing.Angles._

        val angle = atan2(dy, dx)
        val angleZX = atan2(dx, dz)

        val targetX = vector.x + cos(angle) * minDist
        val targetY = vector.y + sin(angle) * minDist

        val targetZ = vector.z + cos(angleZX) * minDist

        val ax = (targetX - ball.vector.x) * spring
        val ay = (targetY - ball.vector.y) * spring
        val az = (targetZ - ball.vector.z) * spring

        velocity = velocity.add(-ax, -ay, -az)

        ball.velocity = ball.velocity.add(ax, ay, az)

        //collisionSound(random(220, 880))
        val mapped = map(vector.z, 0, width, 220, 1320)
        collisionSound(mapped)
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
    // a ball
    usingMatrix {
      translate(vector)
      sphere(diameter / 2)
    }
    // a shadow
    usingMatrix {
      translate(ScalaPVector(vector.x, height, vector.z))
      rotateX(Angles.radians(90))
      ellipse(0, 0, diameter, diameter)
    }
  }

  def collisionSound(freq:Double) {
    import de.sciss.synth._
    import Ops._
    import ugen._

    Synth.play(SuperColliderSynth.sineDecaySynth.name, Seq ("freq" -> freq) )
  }
}

object SoundBall {
  def apply(id: Int)(implicit sp53d: ThreeDimensionalPApp): SoundBall = {
    import sp53d._
    val vector = ScalaPVector(random(width), random(height * 2), 0)
    new SoundBall(vector, random(30, 70), id)
  }
}

object SuperColliderSynth extends LazyLogging {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = {
    val conf = Server.Config()
    // the path to scsynth
    conf.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"
    conf
  }

  val sineDecaySynth = SynthDef("sineDecay") {
    val freq = "freq".kr(261.6)
    val amp = "amp".kr(.2)
    //val env = EnvGen.kr(Env.linen(0.01, 0.98, 0.01, 1), timeScale = 1.0, doneAction = 2)
    val env = EnvGen.kr(Env.perc, doneAction = 2)
    val src = SinOsc.ar(freq).madd(amp * env, 0)
    val pan = Pan2.ar(src * env, 0)
    Out.ar(0, pan)
  }

  def runServer() {

    import scala.concurrent._
    import ExecutionContext.Implicits.global

    val serverPromise = Promise[Server]
    val serverFuture: Future[Server] = serverPromise.future
    serverFuture.foreach(e => println("deferred promised future value = " + e + " : " + sourcecode.Line.generate))

    val runningCodes = (s: Server) => {

      sineDecaySynth.recv(s)

      //  Synth.play (sineDecaySynth.name, Seq ("freq" -> 440) )

      val defaultServer = Server.default
      logger.info("defaultServer=" + defaultServer + " : " + sourcecode.Line.generate)

      serverPromise.success(Server.default)

      logger.info("run ends." + " : " + sourcecode.Line.generate)
      defaultServer
    }


    Server.run(cfg) { s => runningCodes(s) }

    import scala.concurrent.duration._
    Await.result(serverFuture, 10.seconds)

    logger.info("ready" + " : " + sourcecode.Line.generate)
    logger.info("defaultServer=" + serverPromise + " : " + sourcecode.Line.generate)
    logger.info("end" + " : " + sourcecode.Line.generate)

  }

}
