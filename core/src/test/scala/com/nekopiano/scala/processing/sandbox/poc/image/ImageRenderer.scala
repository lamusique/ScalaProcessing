package com.nekopiano.scala.processing.sandbox.poc.image

import com.nekopiano.scala.processing.{CameraMixin, ScalaPApp, ScalaPAppInstanceCompanion, ThreeDimensionalPApp}
import processing.core.PImage

/**
  * Created on 19/09/2016.
  */
class ImageRenderer extends ThreeDimensionalPApp {

  override def settings() {
    size(1280, 720, P3D)
  }
  lazy val luminousBall = createLight(Colors.RED)
  lazy val luminousAlphaBall = createAlphaLight(Colors.GREEN)
  lazy val luminousInvertedAlphaBall = createAlphaLight()
  lazy val luminousMaskedBall = createLight(Colors.RED)

  override def setup() {
    colorMode(HSB, 360, 100, 100, 100)
   //luminousBall = createLight(Colors.RED)
  }

  override def draw() {

    background(64, 80)

    image(luminousBall, 0, 0)
    image(luminousAlphaBall, 200, 0)
    image(luminousInvertedAlphaBall, 400, 0)

    luminousMaskedBall.mask(luminousInvertedAlphaBall)
    image(luminousMaskedBall, 600, 0)

    fill(200, 100, 100, 70)
    rect(0, 300, 100, 100)
  }

  def createLight(colors: Colors): PImage = {
    val side = 150
    val center = side / 2.0f
    val img = createImage(side, side, RGB)

    (0 until side) foreach (y => {
      (0 until side) foreach (x => {
        val distance = (sq(center - x) + sq(center - y)) / 10f
        val c = colors.calculate(distance)
        img.pixels(x + y * side) = c
      })
    })

    img
  }

  def createAlphaLight(colors: Colors): PImage = {
    val side = 150
    val center = side / 2.0f
    val img = createImage(side, side, RGB)

    (0 until side) foreach (y => {
      (0 until side) foreach (x => {
        val distance = (sq(center - x) + sq(center - y)) / 10f
        val c = colors.calculateAlpha(distance)
        img.pixels(x + y * side) = c
      })
    })

    img
  }

  def createAlphaLight(): PImage = {
    val side = 150
    val center = side / 2.0f
    val img = createImage(side, side, RGB)

    (0 until side) foreach (y => {
      (0 until side) foreach (x => {
        val distance = (sq(center - x) + sq(center - y)) / 10f
        //val c = Colors.WHITE.calculateInvertedAlpha(distance)
        val c = Colors.WHITE.calculate(distance)
        img.pixels(x + y * side) = c
      })
    })

    img
  }

}
object ImageRenderer extends ScalaPAppInstanceCompanion {
  override def instance: ScalaPApp = new ImageRenderer with CameraMixin
}



object Colors {

  case object RED extends Colors(8, 4, 4)

  case object ORANGE extends Colors(8, 6, 4)

  case object YELLOW extends Colors(8, 8, 4)

  case object LEAF extends Colors(6, 8, 4)

  case object GREEN extends Colors(4, 8, 4)

  case object EMERALD extends Colors(4, 8, 6)

  case object CYAN extends Colors(4, 8, 8)

  case object SKY extends Colors(4, 6, 8)

  case object BLUE extends Colors(4, 4, 8)

  case object PURPLE extends Colors(6, 4, 8)

  case object MAGENTA extends Colors(8, 4, 8)


  case object WHITE extends Colors(8, 8, 8)


  val values = Array(RED, ORANGE, YELLOW, LEAF, GREEN, EMERALD, CYAN, SKY, BLUE, PURPLE, MAGENTA, WHITE)


  val SUPPRESS = 3f

  def color(a: Float, distance: Float): Int = {
    val color = (256 * a / distance - SUPPRESS).toInt
    return Math.max(0, Math.min(color, 255))
  }

  def alphaColour(distance: Float): Int = {
    val color = (256 * distance - SUPPRESS).toInt
    return Math.max(0, Math.min(color, 255))
  }

  def invertedAlphaColour(a: Float, distance: Float): Int = {
    val color = (256 * distance / a - SUPPRESS).toInt
    return Math.max(0, Math.min(color, 255))
  }
}

sealed abstract class Colors(r: Float, g: Float, b: Float) {

  def calculate(d: Float): Int = 0xff << 24 | Colors.color(r, d) << 16 | Colors.color(g, d) << 8 | Colors.color(b, d)

  //def calculateAlpha(d: Float): Int = 0 << 24 | r.toInt << 16 | g.toInt << 8 | b.toInt
  def calculateAlpha(d: Float): Int = Colors.alphaColour(r) << 24 | Colors.color(r, 0) << 16 | Colors.color(g, 0) << 8 | Colors.color(b, 0)

  def calculateInvertedAlpha(d: Float): Int = 0xff << 24 | Colors.invertedAlphaColour(r, d) << 16 | Colors.invertedAlphaColour(g, d) << 8 | Colors.invertedAlphaColour(b, d)

}
