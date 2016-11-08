package com.nekopiano.scala.sandbox.spec.akka

import akka.actor.{ActorSystem, Props, _}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._


/**
  * Created on 10/09/2016.
  */
object SchedulerTest extends App {

  // create the system and actor
  val system = ActorSystem("AskTestSystem")
  val myActor = system.actorOf(Props[TestActor], name = "myActor")

  // (1) this is one way to "ask" another actor for information
  implicit val timeout = Timeout(5 seconds)
  val future = myActor ? PitchMessage(1, System.currentTimeMillis())
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)

  // (2) a slightly different way to ask another actor for information
  val future2: Future[String] = ask(myActor, PitchMessage(2, System.currentTimeMillis())).mapTo[String]
  val result2 = Await.result(future2, 1 second)
  println(result2)


  //Use system's dispatcher as ExecutionContext
  import system.dispatcher

  //This will schedule to send the Tick-message
  //to the tickActor after 0ms repeating every 50ms
  val cancellable =
  system.scheduler.schedule(
    0 milliseconds,
    500 milliseconds,
    myActor,
    PitchMessage(440, System.currentTimeMillis()))


  Thread.sleep(10000)

  //This cancels further Ticks to be sent
  cancellable.cancel()

  system.terminate()


}

case class PitchMessage(hertz:Double, currentTimeMillis:Long)

class TestActor extends Actor {
  def receive = {
    case msg:PitchMessage => {
      // respond to the 'ask' request
      val response = "hertz = " + msg.hertz + ", currentTimeMillis = " + msg.currentTimeMillis
      println(response)
      sender ! response
    }
    //case a:Any => "This may be an object proper. a=" + a
    case _ => println("This is unexpected.")
  }
}
