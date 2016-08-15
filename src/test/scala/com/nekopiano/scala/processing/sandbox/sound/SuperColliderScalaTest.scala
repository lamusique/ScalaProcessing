package com.nekopiano.scala.processing.sandbox.sound

import com.typesafe.scalalogging.StrictLogging

/**
  * Created on 14/08/2016.
  */
object SuperColliderScalaTest extends App with StrictLogging {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = Server.Config()

  // the path to scsynth
  cfg.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"

//  val defaultServer:

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val serverPromise = Promise[Server]
  val serverFuture: Future[Server] = serverPromise.future
  serverFuture.foreach(e => println("deferred promised future value = " + e + " : " + sourcecode.Line.generate))

//  val runningCodes: (Server) => Unit = (s:Server) => {
val runningCodes = (s:Server) => {

    val synthA = play {
      //      val f = LFSaw.kr(0.4).madd(24, LFSaw.kr(Seq(8.0, 7.23)).madd(3, 80)).midicps
      //      CombN.ar(SinOsc.ar(f) * 0.04, 0.2, 0.2, 4)

      //SinOsc.ar(Seq(440, 211)).madd(0.2, .1)
      //+ SinOsc.ar(440).madd(0.2, .8)

      SinOsc.ar(Seq(440, 220)).madd(0.5, .1)
    }
    val synthB = play {
      Pan2.ar(PinkNoise.ar(0.2).madd(.5, 0), SinOsc.kr(0.5).madd(.5, 0))
    }
    Thread.sleep(2000)
    synthA.free()

    val synthC = play {
      Pan2.ar(SinOsc.ar(261.6 * 2).madd(.2, 0), SinOsc.kr(1).madd(.5, 0))
    }

    val defaultServer = Server.default
    logger.info("defaultServer=" + defaultServer + " : " + sourcecode.Line.generate)

    serverPromise.success(Server.default)

    logger.info("run ends." + " : " + sourcecode.Line.generate)
    defaultServer
  }


  Server.run(cfg){s => runningCodes(s)}


  logger.info("ready" + " : " + sourcecode.Line.generate)
  logger.info("defaultServer=" + serverPromise + " : " + sourcecode.Line.generate)
  logger.info("end" + " : " + sourcecode.Line.generate)

}

