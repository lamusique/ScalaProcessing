package com.nekopiano.scala.sandbox.spec.scala

/**
  * Created on 15/08/2016.
  */
object AopTrait extends App {

  trait Abstract extends Result {
    abstract override def userRepr = "abstract " + super.userRepr
  }

  abstract class Result {
    def userRepr: String = "wtv"
  }

  case class ValDefResult(name: String) extends Result {
    override def userRepr = name
  }

  val a = new ValDefResult("asd") with Abstract
  println(a.userRepr)





  trait Login {
    def login(userName:String,password:String)
  }

  trait LoggableLogin extends Login {
    abstract override def login(userName:String,password:String) = {
      println("logging start")
      super.login(userName,password)
      println("logging end")
    }
  }

  class DefaultLogin extends Login {
    def login(userName:String, password:String) = println("u = %s, p = %s".format(userName, password))
  }

  // mixin
  val login = new DefaultLogin with LoggableLogin
  login.login("user a","password a")



  trait A extends B {
    abstract override def value = "trait A: value = " + super.value
  }

  abstract class B {
    def value: String = "bbb"
  }

  case class C(stringValue: String) extends B {
    override def value = "C: value = " + stringValue
  }

  case class D(stringValue: String) extends B with A {
    override def value = "D: value = " + stringValue
  }


  val c = new C("ccc") with A
  //println(c.stringValue)
  println(c.value)
  val d = new D("ddd")
  //println(d.stringValue)
  println(d.value)
  val da = new D("ddd") with A
  println(da.value)






}

