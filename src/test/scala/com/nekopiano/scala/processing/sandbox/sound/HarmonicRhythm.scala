package com.nekopiano.scala.processing.sandbox.sound

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.nekopiano.scala.processing._
import com.typesafe.scalalogging.LazyLogging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.duration._
import scala.annotation.tailrec
import scala.util.{Failure, Random, Success}

/**
  * Created on 10/09/2016.
  */
object HarmonicRhythm extends App {

  val semitoneRatio = Math.pow(2, 1/12.0)
  val A4 = 440.0

  def transpose(hertz:Double, semitones:Int) = {
    if (semitones > 0) hertz * Math.pow(2, semitones/12.0) else hertz / Math.pow(2, -semitones/12.0)
  }

  val C3 = transpose(A4, -9) / 2


  val theHighestNumber = 16
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


  val system = ActorSystem("HarmonicSystem")
  val playActor = system.actorOf(Props[PlayActor], name = "playActor")
  println("playActor="+playActor)


  // Let's get started
  val toneActors = tones.zipWithIndex.map{case ((pitch, interval), index) => {
    val number = index + 1

    val schedulingActor = system.actorOf(Props[SchedulingActor], "schedulingActor"+ number)

    println("created schedulingActor=" + schedulingActor)
    schedulingActor ! PlayMessage(number, pitch, interval, System.currentTimeMillis())
    schedulingActor
  }}


}

case class PlayMessage(number:Int, hertz:Double, interval:Double, currentTimeMillis:Long)

class SchedulingActor extends Actor {

  val random = new Random()
  def random(max:Double):Double = random.nextDouble() * max

  def receive = {
    case msg:PlayMessage => {

      val response = "hertz = " + msg.hertz + ", interval = " + msg.interval + ", currentTimeMillis = " + msg.currentTimeMillis
      println(response)

      val system = context.system
      import system.dispatcher


      // a single player
      val playActor = system.actorSelection("user/playActor")
      //val playActor = system.actorSelection("akka://HarmonicSystem/user/playActor")

      //val playActor = system.actorOf(Props[PlayActor])

      println("called playActor=" + playActor)

      import scala.concurrent.duration._

      val deviatedStart = random(3000)
      val steppedStart = deviatedStart - deviatedStart % 250
      println("steppedStart=" + steppedStart)

      val cancellable =
        system.scheduler.schedule(
          //0 milliseconds,
          deviatedStart milliseconds,
          msg.interval milliseconds) {
          //println("msg="+msg)
          playActor ! ToneMessage(msg.number, msg.hertz, msg.currentTimeMillis)
        }

      sender ! response
    }
    //case a:Any => "This may be an object proper. a=" + a
    case any:Any => println("This is unexpected. any="+any)
  }

}

case class ToneMessage(number:Int, hertz:Double, currentTimeMillis:Long)
class PlayActor extends Actor {
  def receive = {
    case msg:ToneMessage => {
      play(msg.hertz)
      println("play " + msg)
      //sender ! "Done"
    }
    //case a:Any => "This may be an object proper. a=" + a
    case _ => println("This is unexpected.")
  }
  def play(freq:Double) {
    import de.sciss.synth._
    import Ops._
    import ugen._

    Synth.play(SuperColliderServer.sineDecaySynth.name, Seq ("freq" -> freq) )
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
