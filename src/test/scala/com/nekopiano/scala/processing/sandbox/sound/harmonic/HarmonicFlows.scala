package com.nekopiano.scala.processing.sandbox.sound.harmonic

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging
import de.sciss.synth.Synth

import scala.annotation.tailrec
import scala.concurrent.{Await, Promise}
import scala.language.postfixOps
import scala.util.Random

/**
  * Created on 20/Sep/2016.
  */
object HarmonicFlows extends App {

  val semitoneRatio = Math.pow(2, 1/12.0)
  val A4 = 440.0

  def transpose(hertz:Double, semitones:Int) = {
    if (semitones > 0) hertz * Math.pow(2, semitones/12.0) else hertz / Math.pow(2, -semitones/12.0)
  }

  val C3 = transpose(A4, -9) / 2


  val theHighestNumber = 16
  val harmonicSeries = (1 to theHighestNumber)

  val pitches = harmonicSeries map (C3 * _)


  println(Utility.lcm(harmonicSeries.toList))
  // 720720

  SuperColliderServer.runServer()


  // create the system and actor
  val system = ActorSystem("HarmonicSystem")


  // Let's get started
  val toneActors = pitches.zipWithIndex.map{case (pitch, index) => {
    val number = index + 1

    //val schedulingActor = system.actorOf(Props[SchedulingActor])
    //created schedulingActor=Actor[akka://HarmonicSystem/user/$a#675789708]
    //created schedulingActor=Actor[akka://HarmonicSystem/user/$b#-950137678]

    val toneActor = system.actorOf(Props[ToneActor], "toneActor"+ number)

    println("created toneActor=" + toneActor)
    val amp = 0.3 / number
    val phase = Utility.random(0, 2 * math.Pi)

    toneActor ! ToneMessage(number, pitch, phase, amp, System.currentTimeMillis())
    toneActor
  }}


}

object Utility {

  @tailrec
  def gcd(a: Int, b: Int):Int = if (b == 0) a.abs else gcd(b, a % b)
  def lcm(a: Int, b: Int):Int = (a * b).abs / gcd(a,b)
  def gcd(integers: List[Int]):Int = integers.reduce((l, r) => gcd(l, r))
  def lcm(integers: List[Int]):Int = integers.reduce((l, r) => lcm(l, r))


  val random = new Random()
  def random(max:Double):Double = random.nextDouble() * max
  def random(low: Double, high: Double): Double = {
    if (low >= high) return low
    val diff = high - low
    random(diff) + low
  }
}


case class ToneMessage(number:Int, freq:Double, phase:Double, amp:Double, currentTimeMillis:Long)
case class ToneChangeMessage(number:Int, freq:Option[Double], amp:Option[Double], pan:Option[Double], freqLag:Option[Double], ampLag:Option[Double], panLag:Option[Double], currentTimeMillis:Long)
class ToneActor extends Actor {

  import de.sciss.synth._
  import Ops._
  import ugen._

  import scala.concurrent.duration._

  val system = context.system
  import system.dispatcher

  val synthPromise = Promise[Synth]
  lazy val synth:Synth = Await.result(synthPromise.future, 5 seconds)

  var number = -1
  var freq = 0.0
  var amp = 0.0
  var freqLag = 0.0
  var ampLag = 0.0
  var pan = 0.0
  var panLag = 0.0

  def receive = {
    case msg:ToneMessage => {

      number = msg.number
      freq = msg.freq
      amp = msg.amp
      play(msg.freq, msg.amp)
      println(self + " plays " + msg)

      //sender ! "Done"

      println(synth.productIterator.mkString("\n"))

      changeTone(ToneChangeMessage(number, Option(freq), Option(amp), Option(0), Option(0),Option(0),Option(0),System.currentTimeMillis()))

      sender ! this + " started."
    }
    case msg:ToneChangeMessage => {


      changeTone(msg)
    }
    //case a:Any => "This may be an object proper. a=" + a
    case _ => println("This is unexpected.")
  }

  def play(freq:Double, amp:Double) {
    import de.sciss.synth._
    import Ops._

    val synthLocal = Synth.play(SuperColliderServer.sineLagSynth.name, Seq ("freq" -> freq, "amp" -> amp) )
    synthPromise.success(synthLocal)
  }

  def changeTone(msg:ToneChangeMessage) {

    msg.freq match {
      case None => // do nothing
      case Some(freq) => {
        synth.set("freq" -> freq)
        this.freq = freq
      }
    }
    msg.freqLag match {
      case None => // do nothing
      case Some(freqLag) => {
        synth.set("freqLag" -> freqLag)
        this.freqLag = freqLag
      }
    }
    msg.amp match {
      case None => // do nothing
      case Some(amp) => {
        synth.set("amp" -> amp)
        this.amp = amp
      }
    }
    msg.ampLag match {
      case None => // do nothing
      case Some(ampLag) => {
        synth.set("ampLag" -> ampLag)
        this.ampLag = ampLag
      }
    }
    msg.pan match {
      case None => // do nothing
      case Some(pan) => {
        synth.set("pan" -> pan)
        this.pan = pan
      }
    }
    msg.panLag match {
      case None => // do nothing
      case Some(panLag) => {
        synth.set("panLag" -> panLag)
        this.panLag = panLag
      }
    }


    import Utility._

    val delay = random(20)

    val ampDiff = random(-0.2, 0.2)
    amp = amp + ampDiff
    if (amp < 0 ) amp = 0
    if (amp > 0.2 ) amp = 0.2
    ampLag = random(20)

    val freqDiff = random(-50, 50)
    //val freqDiff = random(-200, 200)
    freq = freq + freqDiff
    freqLag = random(20)

    pan = random(-1, 1)
    panLag = random(20)




    val change = ToneChangeMessage(number, Option(freq), Option(amp), Option(pan), Option(freqLag), Option(ampLag), Option(panLag), System.currentTimeMillis())
    //val change = ToneChangeMessage(number, None, Option(amp), Option(pan), Option(freqLag), Option(ampLag), Option(panLag), System.currentTimeMillis())

    val cancellable = context.system.scheduler.scheduleOnce(delay seconds, self, change)
    println(self + " changes " + change + " after " + delay + " seconds.")
  }

}


object SuperColliderServer extends LazyLogging {

  import de.sciss.synth._
  import Ops._
  import ugen._

  val cfg = {
    val conf = Server.Config()
    // the path to scsynth
    conf.program = "/Applications/SuperCollider.app/Contents/Resources/scsynth"
    conf
  }


  val sineLagSynth = SynthDef("sineLag") {
    val freq = "freq".kr(261.6)
    val freqlag = "freqLag".kr(1.0)
    val phase = "phase".kr(0.0)
    val amp = "amp".kr(.2)
    val amplag = "ampLag".kr(1.0)
    val pan = "pan".kr(0.0)
    val panLag = "panLag".kr(1.0)

    val lagFreq = Lag.kr(freq, freqlag)
    val lagAmp = Lag.kr(amp, amplag)
    val lagPan = Lag.kr(pan, panLag)

    val src = SinOsc.ar(lagFreq, phase).madd(lagAmp, 0)
    val pan2 = Pan2.ar(src, lagPan)
    Out.ar(0, pan2)
  }

  def runServer() {

    import scala.concurrent._
    import ExecutionContext.Implicits.global

    val serverPromise = Promise[Server]
    val serverFuture: Future[Server] = serverPromise.future
    serverFuture.foreach(e => println("deferred promised future value = " + e + " : " + sourcecode.Line.generate))

    val runningCodes = (s: Server) => {

      sineLagSynth.recv(s)

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
