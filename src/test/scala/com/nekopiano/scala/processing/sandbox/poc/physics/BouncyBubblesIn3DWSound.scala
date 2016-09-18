package com.nekopiano.scala.processing.sandbox.poc.physics

import com.nekopiano.scala.processing._
import com.typesafe.scalalogging.LazyLogging

/**
  * Created on 09/08/2016.
  */
class BouncyBubblesIn3DWSoundApp extends ThreeDimensionalCameraPApp with LazyLogging {

  implicit val sp5 = this

  val numBalls = 32
  var balls: Set[Ball] = null

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

    val localBalls = (0.until(numBalls)).map(i => {
      SoundBall.apply(i)
    }).toSet[Ball]
    localBalls.foreach(ball => {ball.others = localBalls.-(ball)})
    balls = localBalls

    logger.info("waiting")
    Thread.sleep(5000)
  }

  override def drawObjects(): Unit = {
    background(0, 0, 0)

    lights()

    fill(255, 100, 100, 100)


    balls.foreach(ball => {
      ball.collide()
      ball.move()
      ball.display()
    })

    usingMatrix {
      val gravityVector = ScalaPVector.apply(width./(2), height, 0)
      val heavenVector = ScalaPVector.apply(width./(2), 0, 0)
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


class SoundBall(vectorConveryor: ScalaPVector, override val diameter: Float, override val id: Int)(override implicit val sp53d: ThreeDimensionalPApp) extends Ball(vectorConveryor, diameter, id) with LazyLogging {

  // If you use a vector in this SoundBall class, e.g.
  // println(vectorConveryor)
  // then after compilation the SoundBall class has it as a property.
  // However if you never use it, then the SoundBall doesn't.


  import sp53d._

  //override var others = Set.empty[SoundBall]

  override def actInCollision() {
    //collisionSound(random(220, 880))
    val mapped = map(vector.z, 0, width, 220, 1320)
    collisionSound(mapped)
  }

  def collisionSound(freq:Double) {
    import de.sciss.synth._
    import Ops._
    import ugen._

    Synth.play(SuperColliderSynth.sineDecaySynth.name, Seq.apply ("freq".->(freq)) )
  }
}

object SoundBall {
  def apply(id: Int)(implicit sp53d: ThreeDimensionalPApp): SoundBall = {
    import sp53d._
    val vector = ScalaPVector.apply(random(width), random(height.*(2)), 0)
    new SoundBall(vector, random(30, 70), id)
  }
}

object SuperColliderSynth extends LazyLogging {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = {
    val conf = Server.Config.apply()
    // the path to scsynth
    conf.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"
    conf
  }

  val sineDecaySynth = SynthDef.apply("sineDecay") {
    val freq = "freq".kr(261.6)
    val amp = "amp".kr(.2)
    //val env = EnvGen.kr(Env.linen(0.01, 0.98, 0.01, 1), timeScale = 1.0, doneAction = 2)
    val env = EnvGen.kr(Env.perc, doneAction = 2)
    val src = SinOsc.ar(freq).madd(amp.*(env), 0)
    val pan = Pan2.ar(src.*(env), 0)
    Out.ar(0, pan)
  }

  def runServer() {

    import scala.concurrent._
    import ExecutionContext.Implicits.global

    val serverPromise = Promise[Server]
    val serverFuture: Future[Server] = serverPromise.future
    serverFuture.foreach(e => println("deferred promised future value = " + e + " : ".+(sourcecode.Line.generate)))

    val runningCodes = (s: Server) => {

      sineDecaySynth.recv(s)

      //  Synth.play (sineDecaySynth.name, Seq ("freq" -> 440) )

      val defaultServer = Server.default
      logger.info("defaultServer=" + defaultServer + " : " + sourcecode.Line.generate)

      serverPromise.success(Server.default)

      logger.info("run ends." + " : " + sourcecode.Line.generate)
      defaultServer
    }


    Server.run(cfg) { s => runningCodes.apply(s) }

    import scala.concurrent.duration._
    Await.result(serverFuture, 10.seconds)

    logger.info("ready" + " : " + sourcecode.Line.generate)
    logger.info("defaultServer=" + serverPromise + " : " + sourcecode.Line.generate)
    logger.info("end" + " : " + sourcecode.Line.generate)

  }

}
