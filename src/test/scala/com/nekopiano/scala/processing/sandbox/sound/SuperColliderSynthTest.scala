package com.nekopiano.scala.processing.sandbox.sound

import scala.annotation.tailrec

/**
  * Created on 14/08/2016.
  */
object SuperColliderSynthTest extends App {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = Server.Config()

  // the path to scsynth
  cfg.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"

  //val conn = Server.boot(config = cfg)

  // runs a server and executes the function
  // when the server is booted, with the
  // server as its argument
  //var synthC:Synth = null

  @volatile private var isReady = false

//  val defaultServer:

//  val x: () => Long = () => System.currentTimeMillis
//  val y: (Int) => Long = (i:Int) => System.currentTimeMillis
//  val z: (Int) => Unit = (i:Int) => System.currentTimeMillis

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val serverPromise = Promise[Server]
  val serverFuture: Future[Server] = serverPromise.future
  serverFuture.foreach(e => println("deferred promised future value = " + e))

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
    println("defaultServer=" + defaultServer)

    isReady = true
    println("run ends. isReady=" + isReady)

    serverPromise.success(Server.default)

    isReady
  }







  Server.run(cfg){s => runningCodes(s)}


  println("ready")


  // doesn't work.
  //@tailrec
  def ready(isReady:Boolean):Boolean = {
    def checkReady(isReady:Boolean):Boolean = {
      Thread.sleep(500)
      println("isReady=" + isReady + " " + System.currentTimeMillis())
      if(isReady) isReady else checkReady(isReady)
    }
    checkReady(isReady)
  }
  ready(isReady)


//  val defaultServer = Server.default
//    println("defaultServer=" + defaultServer)


  println("end")

}

