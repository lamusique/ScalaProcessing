package com.nekopiano.scala.sandbox.spec.scala

/**
  * Created by neko on 27/07/2016.
  */
trait SubClassReferenceTrait {

  def main(args: Array[String]): Unit = {
    println(this.getClass.getCanonicalName)
  }

}
class SubClassReference {

}
object SubClassReference extends SubClassReferenceTrait {
  //com.nekopiano.scala.sandbox.spec.scala.SubClassReference$
}
