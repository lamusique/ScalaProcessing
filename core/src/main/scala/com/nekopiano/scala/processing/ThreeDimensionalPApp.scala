package com.nekopiano.scala.processing

import processing.core.PApplet

/**
  * Created on 27/07/2016.
  */
trait ThreeDimensionalPApp extends ScalaPApp with ScalaPConstants {

  // ================
  // Coordination in 3D
  // ================
  def screen(vector: ScalaPVector) =
  ScalaPVector(screenX(vector.x, vector.y, vector.z), screenY(vector.x, vector.y, vector.z), screenZ(vector.x, vector.y, vector.z))
  def model(vector: ScalaPVector) =
    ScalaPVector(modelX(vector.x, vector.y, vector.z), modelY(vector.x, vector.y, vector.z), modelZ(vector.x, vector.y, vector.z))
  def translate(vector: ScalaPVector):Unit = translate(vector.x, vector.y, vector.z)


  // ================
  // Drawing
  // ================

  override def text(txt:String, vector: ScalaPVector):Unit = {
    text(txt, vector.x, vector.y, vector.z)
  }

  override def point(vector:ScalaPVector): Unit = {
    point(vector.x, vector.y, vector.z)
  }

  override def line(start: ScalaPVector, end:ScalaPVector): Unit = {
    line(start.x, start.y, start.z, end.x, end.y, end.z)
  }

  // ================
  // Drawing in 3D
  // ================
  def box(vector: ScalaPVector): Unit = {
    box(vector.x, vector.y, vector.z)
  }

}
