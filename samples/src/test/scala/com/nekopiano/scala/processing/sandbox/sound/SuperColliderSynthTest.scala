package com.nekopiano.scala.processing.sandbox.sound

import com.typesafe.scalalogging.{LazyLogging, StrictLogging}

import scala.annotation.tailrec

/**
  * Created on 14/08/2016.
  */
object SuperColliderSynthTest extends App with LazyLogging {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = Server.Config()

  // the path to scsynth
  cfg.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"


  import scala.concurrent._
  import ExecutionContext.Implicits.global

  val serverPromise = Promise[Server]
  val serverFuture: Future[Server] = serverPromise.future
  serverFuture.foreach(e => println("deferred promised future value = " + e + " : " + sourcecode.Line.generate))


  //  SynthDef.new( \sound2, { arg freq, amp;
  //    var src, env;
  //    src = SinOsc.ar( freq, 0, 1, 0 );
  //    env = EnvGen.kr(
  //      Env.new( [0, 1, 0.3, 0], [0.01, 0.3, 0.45] ),
  //    levelScale: amp,
  //    doneAction: 2
  //    );
  //    Out.ar( 0, src*env );
  //  }).send(s);

  val sineSynth = SynthDef("sine") {
    val freq = "freq".kr(261.6)
    val amp = "amp".kr(.2)
    val a = SinOsc.ar(freq).madd(amp, 0)
    val b = Pan2.ar(a, 0)
    Out.ar(0, b)
  }

  val sineAmpSynth = SynthDef("sineAmp") {
    val freq = "freq".kr(261.6)
    val xline = XLine.kr(.9, .1, 10)
    val a = SinOsc.ar(freq).madd(xline, 0)
    val b = Pan2.ar(a, 0)
    Out.ar(0, b)
  }

  //  SynthDef("testsynth", {
  //    arg out, sustain=1, pan;
  //    var env = EnvGen.ar(Env.linen(0.01, 0.98, 0.01, 1,-3), timeScale:sustain, doneAction:2);
  //    var sound = SinOsc.ar(440.0);
  //    OffsetOut.ar(out, DirtPan.ar(sound, ~dirt.numChannels, pan, env));
  //  }).add;

  val sineDecaySynth = SynthDef("sineDecay") {
    val freq = "freq".kr(261.6)
    val amp = "amp".kr(.2)
    //val env = EnvGen.kr(Env.linen(0.01, 0.98, 0.01, 1), timeScale = 1.0, doneAction = 2)
    val env = EnvGen.kr(Env.perc, doneAction = 2)
    val src = SinOsc.ar(freq).madd(amp * env, 0)
    val pan = Pan2.ar(src * env, 0)
    Out.ar(0, pan)
  }

  val analogBubblesSynth = SynthDef("AnalogBubbles") {
    val f1 = "freq1".kr(0.4)
    val f2 = "freq2".kr(8.0)
    val d = "detune".kr(0.90375)
    val f = LFSaw.ar(f1).madd(24, LFSaw.ar(Seq(f2, f2 * d)).madd(3, 80)).midicps // glissando function
    val x = CombN.ar(SinOsc.ar(f) * 0.04, 0.2, 0.2, 4) // echoing sine wave
    Out.ar(0, x)
  }

  //  val runningCodes: (Server) => Unit = (s:Server) => {
  val runningCodes = (s: Server) => {

    serverPromise.success(Server.default)
    val defaultServer = Server.default
    logger.info("defaultServer=" + defaultServer + " : " + sourcecode.Line.generate)


    // Mandatory!
    // prepare synths
    sineDecaySynth.recv(s)
    sineSynth.recv(s)
    sineAmpSynth.recv(s)


    Synth.play(sineDecaySynth.name, Seq("freq" -> 440))
    Synth.play(sineDecaySynth.name, Seq("freq" -> 450))
    Synth.play(sineDecaySynth.name, Seq("freq" -> 460))

    //sineDecaySynth.play(freq = 440)
    //analogBubblesSynth.play()

    val sine = Synth.play(sineSynth.name, Seq("freq" -> 261.6))

    println(sineSynth.graph.productIterator.toList)
    println(sineSynth.graph.constants)
    println(sineSynth.graph.controlNames)
    println(sineSynth.graph.controlValues)

    Thread.sleep(2000)
    sine.set(("amp" -> .1))
    Thread.sleep(2000)
    sine.set(("amp" -> .8))
    Thread.sleep(2000)
    //sine.free()
    sine.run(false)

    val sineAmp = Synth.play(sineAmpSynth.name, Seq("freq" -> 440))

    sine.run()
    //sine.map(("amp" -> XLine.kr(.9, .1, 10)))


    logger.info("run ends." + " : " + sourcecode.Line.generate)
    defaultServer
  }


  Server.run(cfg) { s => runningCodes(s) }


  logger.info("ready" + " : " + sourcecode.Line.generate)
  logger.info("defaultServer=" + serverPromise + " : " + sourcecode.Line.generate)
  logger.info("end" + " : " + sourcecode.Line.generate)

}

