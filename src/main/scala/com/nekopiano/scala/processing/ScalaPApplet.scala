package com.nekopiano.scala.processing

import processing.core.PApplet

/**
 * Created by neko on 2015/11/23.
 */
class ScalaPApplet extends PApplet with ScalaPConstants {
  import processing.core.PApplet._

  // Utitilies

  // decouple the matrix using loan pattern
  def usingMatrix(f: => Unit):Unit = {
    pushMatrix()
    f
    popMatrix()
  }

  // Drawing
  def line(start: ScalaPVector, end:ScalaPVector): Unit = {
    line(start.x, start.y, start.z, end.x, end.y, end.z)
  }

  def box(vector: ScalaPVector): Unit = {
    box(vector.x, vector.y, vector.z)
  }

  // Mathematics
  def sin(angle:Float) = PApplet.sin(angle)

}
object ScalaPApplet {
  def main(args : Array[String]) = PApplet.main(args)
}
