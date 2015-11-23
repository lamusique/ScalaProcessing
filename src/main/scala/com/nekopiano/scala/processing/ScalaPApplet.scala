package com.nekopiano.scala.processing

import processing.core.PApplet

/**
 * Created by neko on 2015/11/23.
 */
class ScalaPApplet extends PApplet {
  import processing.core.PApplet._

  def box(vector: ScalaPVector): Unit = {
    box(vector.x, vector.y, vector.z)
  }

}
object ScalaPApplet {
  def main(args : Array[String]) = PApplet.main(args)
}
