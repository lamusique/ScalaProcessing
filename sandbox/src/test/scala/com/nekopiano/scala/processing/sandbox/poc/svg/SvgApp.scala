package com.nekopiano.scala.processing.sandbox.poc.svg

import com.nekopiano.scala.processing.{ScalaPAppCompanion, TwoDimensionalPApp}
import processing.core.PGraphics

/**
  * Created on 15/08/2016.
  */
class SvgApp extends TwoDimensionalPApp {

  override def settings(): Unit = {
    size(640, 480)
  }

  override def draw(): Unit = {

  // PApplet case
  beginRecord(SVG, "PApplet-sample.svg")
  noStroke()
  fill(0, 200, 250)
  ellipse(width/2, height/2, 100, 100)
  endRecord()

  // PGraphics case
  val pg = createGraphics(640, 480, SVG, "PGraphics-sample.svg");
  pg.beginDraw();
  pg.ellipse(pg.width/2, pg.height/2, 100, 100);
  pg.endDraw();
  }

}

object SvgApp extends ScalaPAppCompanion {}
