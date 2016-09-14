package com.nekopiano.scala.sandbox.spec.scala

import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

import scala.util.Random

/**
  * Created on 14/08/2016.
  */
object Deferred extends App {

  // https://en.wikipedia.org/wiki/Futures_and_promises


  // Future

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  val futureProcess: Future[String] = Future {
    val now = new Date
    "Secret Future at " + now}
  // After Future ends this foreach will be run.
  futureProcess.foreach(e => println("deferred future value = " + e))
  println("Future code passed.")


  // Promise

  import scala.concurrent._
  // This Promise will have a String value in future.
  val promisedString = Promise[String]

  // Set a result.
  promisedString.success("a successful value")
  // promisedString.success("Twice")
  //   -> java.lang.IllegalStateException: Promise already completed.
  println("Promise code passed.")


  // Promise -> Future

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val promise: Promise[String] = Promise[String]
  val future: Future[String] = promise.future
  future.foreach(e => println("deferred promised future value = " + e))
  promise.success("hello successful promise")
  println("Promise and Future mixed passed.")



  // to wait future process.
  Thread.sleep(100)
  println("Promise -> Future codes passed.")





  val indexHolder = new AtomicInteger(0)
  val random = new Random()
  val promises: Seq[Promise[Int]] = for {i <- 1 to 3} yield Promise[Int]
  val futures: Seq[Future[Int]] = for {i <- 1 to 8} yield Future[Int] {
    val waitMilliSec = random.nextInt(1000)
    println("waitMilliSec=" + waitMilliSec)
    Thread.sleep(waitMilliSec)
    waitMilliSec
  }
  futures.foreach { f => f.onSuccess {case waitMilliSec =>
    val index = indexHolder.getAndIncrement
    if(index < promises.length) {
      promises(index).success(waitMilliSec)
    }
  }}
  promises.foreach { p => p.future.onSuccess{ case waitMilliSec => println(waitMilliSec)}}
  Thread.sleep(5000)




  println("ends.")
}
