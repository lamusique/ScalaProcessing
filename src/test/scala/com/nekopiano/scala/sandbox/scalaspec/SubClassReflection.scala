package com.nekopiano.scala.sandbox.scalaspec

import scala.annotation.tailrec
import scala.reflect.runtime.universe

/**
 * Created by neko on 2015/11/29.
 */
object SubClassReflection {

  def main(args: Array[String]) {

    // as a base type
    val instance: Base = new Terminal

    // assuming its sub type has subValue.
    val subValueFromBase = getFieldValueReflectively(instance, "subValue")
    println("subValueFromBase=" + subValueFromBase)

  }

  def getFieldValueReflectively(instance: AnyRef, fieldName: String) = {
    val m = universe.runtimeMirror(instance.getClass.getClassLoader)

    println(instance.getClass.getTypeName)
    val cs = m.classSymbol(instance.getClass)
    println("cs=" + cs)
    val members = cs.toType.members
    printHierarchy(instance)

    val targetSymbol = members.find(member => {
      val name = member.name.toString
      name == fieldName
    })

    val targetMember = targetSymbol.get.asTerm
    println("targetMember=" + targetMember)

    val im = m.reflect(instance)

    val fieldMirror = im.reflectField(targetMember)
    fieldMirror.get
  }

  def printHierarchy(instance: Any): Unit = {
    val hierarchy = getHierachy(instance).toSeq.reverse
    val hierarchyText = hierarchy.zipWithIndex.map { case (e, i) => {
      ("  " * i) + e
    }
    }
    hierarchyText foreach println
  }

  def getHierachy(instance: Any) = {
    val m = universe.runtimeMirror(instance.getClass.getClassLoader)
    val classHierarchy = Iterator.iterate[Class[_]](instance.getClass)(_.getSuperclass).takeWhile(_ ne null)
    classHierarchy.toSeq
  }

}

class Base {
  val value = 4
}

class Sub extends Base {
  val subValue = 8
}

class Terminal extends Sub {
  var terminalValue = 16
}
