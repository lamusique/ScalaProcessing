package com.nekopiano.scala.processing

import processing.core.PConstants

/**
 * Created on 2015/11/24.
 */
trait ScalaPConstants extends PConstants {

  val JAVA2D = PConstants.JAVA2D
  val FX2D = PConstants.FX2D
  // based on OpenGL
  val P2D = PConstants.P2D
  val P3D = PConstants.P3D

  val PDF = PConstants.PDF
  val SVG = PConstants.SVG
  val DXF = PConstants.DXF

  // Shapes
  val TRIANGLE = PConstants.TRIANGLE
  val TRIANGLES = PConstants.TRIANGLES
  val TRIANGLE_STRIP = PConstants.TRIANGLE_STRIP
  val TRIANGLE_FAN = PConstants.TRIANGLE_FAN


  // Mathematics
  val PI = PConstants.PI
  val DEG_TO_RAD = PConstants.DEG_TO_RAD

  // Keys
  val CODED = PConstants.CODED
  val UP = PConstants.UP
  val DOWN = PConstants.DOWN
  val LEFT = PConstants.LEFT
  val RIGHT = PConstants.RIGHT
  val ALT = PConstants.ALT
  val CONTROL = PConstants.CONTROL
  val SHIFT = PConstants.SHIFT

  // filter/convert types
  val BLUR = PConstants.BLUR

}
