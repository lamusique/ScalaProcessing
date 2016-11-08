package com.nekopiano.scala.processing.sandbox.sound

/**
  * Created on 13/08/2016.
  */
object SuperColliderTest extends App {

  import de.sciss.synth._
  import ugen._
  import Ops._

  val cfg = Server.Config()

  // the path to scsynth
  cfg.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"

  //val conn = Server.boot(config = cfg)

  // runs a server and executes the function
  // when the server is booted, with the
  // server as its argument
  //var synthC:Synth = null
  Server.run(cfg) { s =>

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
  }

//  Thread.sleep(2000)
//  synthC.free()

//  Server.run(cfg) { s =>
//
//    val pc = play {
//      Pan2.ar(SinOsc.ar(261.6 * 2).madd(.2, 0), SinOsc.kr(1).madd(.5, 0))
//    }
//
//  }

}
