package com.nekopiano.scala.sandbox.scalaspec

import scala.reflect.runtime.universe

/**
 * Created by neko on 2015/11/29.
 */
object SubClassReflection {

  def main(args: Array[String]) {

    // as a base type
    val instance:Base = new Sub

    // assuming its sub type has subValue.
    val subValueFromBase = getFieldValueReflectively(instance, "subValue")
    println("subValueFromBase="+subValueFromBase)

  }

  def getFieldValueReflectively(instance:AnyRef, fieldName:String) = {
    val m = universe.runtimeMirror(instance.getClass.getClassLoader)

    println(instance.getClass.getTypeName)
    val theType = getTypeTag(instance.getClass).tpe
    println("theType="+theType)
    val cs = m.classSymbol(instance.getClass)
    println("cs="+cs)

    //val fieldTermSymb = universe.typeOf[instance.type].decl(universe.TermName(fieldName)).asTerm
    val fieldTermSymb = cs.selfType.decl(universe.TermName(fieldName)).asTerm

    val im = m.reflect(instance)
    val fieldMirror = im.reflectField(fieldTermSymb)
    fieldMirror.get
  }

  def getTypeTag[T: universe.TypeTag](targetClass: Class[T]): universe.TypeTag[T] =
    universe.typeTag[T]

}

class Base {
  val value = 4
}
class Sub extends Base {
  val subValue = 8
}
