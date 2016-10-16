package com.nekopiano.scala.sandbox.spec.scala

/**
  * Created on 21/08/2016.
  */
object ImplicitTest extends App {

  trait Trait {
    implicit val value = 123
  }
  object Object {
    implicit val value = 456
    val valueB = 789
  }

  class SuperClass {
    def run(argValue:Int)(implicit impValue:Int) = {argValue + impValue}
  }

  class SubClassA extends SuperClass with Trait {
    def compute() = {run(1)}
  }

  val a = new SubClassA()
  println(a.compute())




  class SubClassB extends SuperClass {
    def compute() = {run(2)(Object.value)}
    def computeB() = {run(3)(Object.valueB)}
  }

  val b = new SubClassB()
  println(b.compute())
  println(b.computeB())

}
