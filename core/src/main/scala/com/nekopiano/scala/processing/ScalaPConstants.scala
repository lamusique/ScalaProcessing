package com.nekopiano.scala.processing

import processing.core.PConstants

/**
 * Created on 2015/11/24.
 */
trait ScalaPConstants extends PConstants {

  import PConstants._

  val JAVA2D = PConstants.JAVA2D
  val FX2D = PConstants.FX2D
  // based on OpenGL
  val P2D = PConstants.P2D
  val P3D = PConstants.P3D

  val PDF = PConstants.PDF
  val SVG = PConstants.SVG
  val DXF = PConstants.DXF

  // hints for OpenGL
  val DISABLE_DEPTH_TEST = PConstants.DISABLE_DEPTH_TEST
  val ENABLE_DEPTH_TEST = PConstants.ENABLE_DEPTH_TEST

  // for colors and/or images
  val RGB = PConstants.RGB
  val ARGB = PConstants.ARGB
  val HSB = PConstants.HSB
  val ALPHA = PConstants.ALPHA

  // blend mode keyword definitions
  // @see processing.core.PImage#blendColor(int,int,int)
  val REPLACE = PConstants.REPLACE
  val BLEND = PConstants.BLEND
  val ADD = PConstants.ADD
  val SUBTRACT = PConstants.SUBTRACT
  val LIGHTEST = PConstants.LIGHTEST
  val DARKEST = PConstants.DARKEST
  val DIFFERENCE = PConstants.DIFFERENCE
  val EXCLUSION = PConstants.EXCLUSION
  val MULTIPLY = PConstants.MULTIPLY
  val SCREEN = PConstants.SCREEN
  val OVERLAY = PConstants.OVERLAY
  val HARD_LIGHT = PConstants.HARD_LIGHT
  val SOFT_LIGHT = PConstants.SOFT_LIGHT
  val DODGE = PConstants.DODGE
  val BURN = PConstants.BURN

  // Shapes
  val TRIANGLE = PConstants.TRIANGLE
  val TRIANGLES = PConstants.TRIANGLES
  val TRIANGLE_STRIP = PConstants.TRIANGLE_STRIP
  val TRIANGLE_FAN = PConstants.TRIANGLE_FAN

  // shape drawing modes
  val CORNER = PConstants.CORNER
  val CORNERS = PConstants.CORNERS
  val RADIUS = PConstants.RADIUS
  val CENTER = PConstants.CENTER

  // Mathematics
  val PI = PConstants.PI
  val TWO_PI = PConstants.TWO_PI
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
