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

  // runs a server and executes the function
  // when the server is booted, with the
  // server as its argument
  Server.run(cfg) { s =>
    // play is imported from package de.sciss.synth.
    // it provides a convenience method for wrapping
    // a synth graph function in an `Out` element
    // and playing it back.
    play {
      //val f = LFSaw.kr(0.4).madd(24, LFSaw.kr(Seq(8, 7.23)).madd(3, 80)).midicps
      val f = LFSaw.kr(0.4).madd(24, LFSaw.kr(8, 7.23).madd(3, 80)).midicps
      CombN.ar(SinOsc.ar(f) * 0.04, 0.2, 0.2, 4)
    }
  }

}
