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
  //val theHighestNumber = 2
  val harmonicSeries = (1 to theHighestNumber)

  val pitches = harmonicSeries map (C3 * _)

  @tailrec
  def gcd(a: Int, b: Int):Int = if (b == 0) a.abs else gcd(b, a % b)
  def lcm(a: Int, b: Int):Int = (a * b).abs / gcd(a,b)
  def gcd(integers: List[Int]):Int = integers.reduce((l, r) => gcd(l, r))
  def lcm(integers: List[Int]):Int = integers.reduce((l, r) => lcm(l, r))

  println(lcm(harmonicSeries.toList))
  // 720720

  // 32nd notes, demisemiquaver
  // quaver = 60, milliseconds
  val demisemiquaver = 1000 / 32
  // 31 milisecs
  //val demisemiquaver = 1000

  val rhythmIntevals = harmonicSeries.map(number => {
    demisemiquaver * (theHighestNumber.toDouble / number)
  })

  val tones = pitches zip rhythmIntevals



  SuperColliderServer.runServer()


  // create the system and actor
  val system = ActorSystem("HarmonicSystem")


  // Let's get started
  val toneActors = tones.zipWithIndex.map{case ((pitch, interval), index) => {
    val number = index + 1

    //val schedulingActor = system.actorOf(Props[SchedulingActor])
    //created schedulingActor=Actor[akka://HarmonicSystem/user/$a#675789708]
    //created schedulingActor=Actor[akka://HarmonicSystem/user/$b#-950137678]

    val toneActor = system.actorOf(Props[ToneActor], "toneActor"+ number)

    println("created toneActor=" + toneActor)
    val amp = 0.5 / number
    toneActor ! ToneMessage(number, pitch, amp, System.currentTimeMillis())
    toneActor
  }}


}

case class ToneMessage(number:Int, hertz:Double, amp:Double, currentTimeMillis:Long)
case class ToneChangeMessage(number:Int, hertz:Option[Double], amp:Option[Double], currentTimeMillis:Long)
class ToneActor extends Actor {

  import de.sciss.synth._
  import Ops._
  import ugen._

  import scala.concurrent.duration._

  val system = context.system
  import system.dispatcher

  val random = new Random()
  def random(max:Double):Double = random.nextDouble() * max
  def random(low: Double, high: Double): Double = {
    if (low >= high) return low
    val diff = high - low
    random(diff) + low
  }

  val synthPromise = Promise[Synth]
  lazy val synth:Synth = Await.result(synthPromise.future, 5 seconds)

  var number = -1
  var freq = 0.0
  var amp = 0.0

  def receive = {
    case msg:ToneMessage => {

      this.number = msg.number
      this.freq = msg.hertz
      this.amp = msg.amp
      play(msg.hertz, msg.amp)
      println(this + " plays " + msg)

      //sender ! "Done"

      changeTone()

      sender ! this + " started."
    }
    case msg:ToneChangeMessage => {
      msg.hertz match {
        case None => // do nothing
        case Some(hertz) => changePitch(hertz)
      }
      msg.amp match {
        case None => // do nothing
        case Some(amp) => changeAmp(amp)
      }

      changeTone()
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

  def changeTone() {
    val delay = random(5)
    val amp = random(0.001, 0.5)
    val freqDiff = random(-20, 20)
    val freq = this.freq + freqDiff

    //val change = ToneChangeMessage(number, None, Option(amp), System.currentTimeMillis())
    val change = ToneChangeMessage(number, Option(freq), Option(amp), System.currentTimeMillis())

    val cancellable = context.system.scheduler.scheduleOnce(delay seconds, self, change)
    println(this + " changes " + change + " after " + delay + " seconds.")
  }

  def changePitch(freq:Double) {
    synth.set("freq" -> freq)
    this.freq = freq
  }
  def changeAmp(amp:Double) {
    synth.set("amp" -> amp)
    this.amp = amp
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
    val amp = "amp".kr(.2)
    val freqlag = "freqlag".kr(2.0)
    val amplag = "amplag".kr(2.0)

    val lagFreq = Lag.kr(freq, freqlag)
    val lagAmp = Lag.kr(amp, amplag)

    val src = SinOsc.ar(lagFreq).madd(lagAmp, 0)
    val pan = Pan2.ar(src, 0)
    Out.ar(0, pan)
  }

  def runServer() {

    import scala.concurrent._
    import ExecutionContext.Implicits.global

    val serverPromise = Promise[Server]
    val serverFuture: Future[Server] = serverPromise.future
    serverFuture.foreach(e => println("deferred promised future value = " + e + " : " + sourcecode.Line.generate))

    val runningCodes = (s: Server) => {

      sineLagSynth.recv(s)

//      val a = Synth.play (sineLagSynth.name, Seq ("freq" -> 440) )
//      a.set()

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
