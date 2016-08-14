package com.nekopiano.scala.sandbox.spec.scala

import java.util.Date

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
  println("Future code ends.")


  // Promise

  import scala.concurrent._
  // This Promise will have a String value in future
  val promisedString: Promise[String] = Promise[String]

  promisedString.success("a successful value")
  //b.success("Twice")
  // -> java.lang.IllegalStateException: Promise already completed.
  println("Promise code ends.")


  // Promise -> Future

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val promise: Promise[String] = Promise[String]
  val future: Future[String] = promise.future
  future.foreach(e => println("deferred promised future value = " + e))
  println("Promise and Future mixed ends.")
  promise.success("hello successful promise")




  // to wait future process.
  Thread.sleep(100)
  println("ends.")
}
