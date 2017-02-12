package com.nekopiano.scala.processing

import processing.core.PApplet

/**
  * Created on 27/07/2016.
  */
trait TwoDimensionalPApp extends ScalaPApp with ScalaPConstants {

  // ================
  // Coordination in 2D
  // ================
  def translate(vector: ScalaPVector):Unit = translate(vector.x, vector.y)

  // ================
  // Drawing
  // ================

  override def text(txt:String, vector: ScalaPVector):Unit = {
    text(txt, vector.x, vector.y)
  }

  override def point(vector:ScalaPVector): Unit = {
    point(vector.x, vector.y)
  }

  override def line(start: ScalaPVector, end:ScalaPVector): Unit = {
    line(start.x, start.y, end.x, end.y)
  }

}
