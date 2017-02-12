package com.nekopiano.scala.sandbox.spec.scala

import scala.annotation.tailrec

/**
  * Created on 15/08/2016.
  */
object AopTrait extends App {



  abstract class SuperAbstract {
    def value: String = "super abstract"
  }

  trait AbstractTrait extends SuperAbstract {
    abstract override def value = "abstract trait : " + super.value
  }

  case class CaseClassA(name: String) extends SuperAbstract {
    override def value = "CaseClassA name=" + name + ", super = " + super.value
  }


  case class CaseClassB(name: String) extends SuperAbstract with AbstractTrait {
    override def value = "CaseClassB name=" + name + ", super = " + super.value
  }

  println("=" * 64)

  def scanHierarchy(obj:Any) = {
    @tailrec
    def getSuper(clazz:Class[_], acc:List[Class[_]]):List[Class[_]] = {
      clazz match {
        case null => acc
        case clazz:Class[_] =>
          getSuper(clazz.getSuperclass, acc :+ clazz)
      }
    }
    getSuper(obj.getClass, List())
  }
  def printClassHierarchy(obj:Any) = {
    val classHierarchy = scanHierarchy(obj)
    val formattedClassHierarchy = classHierarchy.reverse.zipWithIndex map { case (clazz, index) => {
      if (index > 0)
        index + ": " + "  " * index + "-> " + clazz
      else index + ": " + clazz
    }}
    println(formattedClassHierarchy.mkString("\n"))
  }


  val caseClassAWithTrait = new CaseClassA("CaseClass A at runtime with Abstract") with AbstractTrait
  printClassHierarchy(caseClassAWithTrait)
  println(caseClassAWithTrait.value)

  val caseClassB = new CaseClassB("CaseClass B")
  printClassHierarchy(caseClassB)
  println(caseClassB.value)

  val caseClassBWithTrait = new CaseClassB("CaseClass B at runtime with Abstract") with AbstractTrait
  printClassHierarchy(caseClassBWithTrait)
  println(caseClassBWithTrait.value)

  println("=" * 64)

  trait TraitA {
    def greet(): Unit = println("I'm Trait A.")
  }

  trait TraitB extends TraitA {
    override def greet(): Unit = {
      super.greet()
      println("I'm Trait B.")
    }
  }

  trait TraitC extends TraitA {
    override def greet(): Unit = {
      super.greet()
      println("I'm Trait C.")
    }
  }

  class ClassA extends TraitB with TraitC
  class ClassB extends TraitC with TraitB
  class ClassC extends TraitB with TraitC {
    override def greet(): Unit = {
      super.greet()
      println("I'm Class C.")
    }
  }
  class ClassD extends TraitA {
    override def greet(): Unit = {
      super.greet()
      println("I'm Class D.")
    }
  }
  class ClassE extends TraitA with TraitB {
    override def greet(): Unit = {
      super.greet()
      println("I'm Class E.")
    }
  }

  val classA = new ClassA
  printClassHierarchy(classA)
  classA.greet()

  val classB = new ClassB
  printClassHierarchy(classB)
  classB.greet()

  val classC = new ClassC
  printClassHierarchy(classC)
  classC.greet()

  val classCWithTrait = new ClassC with TraitA
  printClassHierarchy(classCWithTrait)
  classCWithTrait.greet()

  val classDWithTrait = new ClassD with TraitB
  printClassHierarchy(classDWithTrait)
  classDWithTrait.greet()

  val classE = new ClassE
  printClassHierarchy(classE)
  classE.greet()

  println("=" * 64)


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

