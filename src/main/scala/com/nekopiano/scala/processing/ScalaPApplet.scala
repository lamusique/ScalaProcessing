package com.nekopiano.scala.processing

import processing.core.PApplet

/**
 * Created by neko on 2015/11/23.
 */
class ScalaPApplet extends PApplet {
  import processing.core.PApplet._

  def line(start: ScalaPVector, end:ScalaPVector): Unit = {
    line(start.x, start.y, start.z, end.x, end.y, end.z)
  }

  def box(vector: ScalaPVector): Unit = {
    box(vector.x, vector.y, vector.z)
  }

}
object ScalaPApplet {
  def main(args : Array[String]) = PApplet.main(args)
}
