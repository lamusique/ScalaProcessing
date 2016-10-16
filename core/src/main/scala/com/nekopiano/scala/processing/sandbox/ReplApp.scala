package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppCompanion, ScalaPVector, TwoDimensionalPApp}
import processing.core.PApplet

/**
  *

Try a reactive programming with sbt console.
```
$ sbt console
scala> val ra = com.nekopiano.scala.processing.sandbox.ReplApp
scala> ra.variable = 23
```

The object doesn't reload itself but the class does on IntelliJ debug mode after rebuilding.

  * Created on 03/09/2016.
  */
class ReplApp extends TwoDimensionalPApp {

  import ReplApp._
  //  val value = 11
  //  var variable = 22

  override def settings: Unit = {
    size(800, 600)
  }

  override def draw: Unit = {
    background(0)

    val second = System.currentTimeMillis() % 1000
    text("second=" + second, width / 2, height / 3)
    if (second > 250) {
      text("value=" + value, width / 2, height / 2)
      text("variable=" + variable, width / 2, height * (9f/16))
      text("f=" + f(), width / 2, height * (10f/16))
    }

  }

}
object ReplApp extends ScalaPAppCompanion {
  val value = 10
  // These variables can be changed on SBT REPL.
  var variable = 20
  var f: ()=> Float = () => {value + variable + System.currentTimeMillis() % 10000}
}
