package com.nekopiano.scala.processing.sandbox.poc.colour

import com.nekopiano.scala.processing.{CameraMixin, ThreeDimensionalPApp}
import processing.core.PApplet

import scala.concurrent.{Await, Promise}

/**
  * Created on 14/09/2016.
  */
class ColourPlay extends ThreeDimensionalPApp {


  // Constants
  object Axis extends Enumeration {
    type Axis = Value
    val Y, X, Z = Value
  }
  import Axis._

  import scala.concurrent.duration._

  val b1 = Promise[Int]
  val b2 = Promise[Int]
  val c1 = Promise[Int]
  val c2 = Promise[Int]
  // blocking
  lazy val b1r = Await.result(b1.future, 3 seconds)
  lazy val b2r = Await.result(b2.future, 3 seconds)
  lazy val c1r = Await.result(c1.future, 3 seconds)
  lazy val c2r = Await.result(c2.future, 3 seconds)


  val dim = Promise[Int]
  lazy val dimR = Await.result(dim.future, 3 seconds)


  override def settings(): Unit = {
    //size(640, 360)
    size(1024, 768, P3D)

  }

  override def setup() {

    // Define colors
    b1 success color(255)
    b2 success color(0)
    c1 success color(204, 102, 0)
    c2 success color(0, 102, 153)

    //noLoop()

    dim success width/2
    background(0)
    colorMode(HSB, 360, 100, 100)
    noStroke()
    ellipseMode(RADIUS)
    //frameRate(1)
    //frameRate(60)

    smooth()
    pixelDensity(pixelDensity)

  }




  var bgC = 0
  override def draw() {

    surface.setTitle("FPS: " + frameRate + " background: " + bgC)

    //background(0)
    background(bgC)
    bgC += 1

    // Background
    setGradient(0, 0, width/2, height, b1r, b2r, X);
    setGradient(width/2, 0, width/2, height, b2r, b1r, X);
    // Foreground
    setGradient(50, 90, 540, 80, c1r, c2r, Y);
    setGradient(50, 190, 540, 80, c2r, c1r, X);

    (0 to (width / dimR)).foreach(i => {
      drawGradient(i * dimR, height/2)
      //Thread.sleep(500)
    })
  }

  def setGradient(x:Int, y:Int, w:Float, h:Float, c1:Int, c2:Int, axis:Axis) {

    noFill()

    if (axis == Y) {  // Top to bottom gradient

      (y to y + h.toInt).foreach(i => {
        val inter = map(i, y, y+h, 0, 1)
        val c = lerpColor(c1, c2, inter)
        usingStyle {
          stroke(c)
          line(x, i, x + w, i)
        }
      })

    }
    else if (axis == X) {  // Left to right gradient

      (y to y + w.toInt).foreach(i => {
        val inter = map(i, x, x+w, 0, 1)
        val c = lerpColor(c1, c2, inter)
        usingStyle {
          stroke(c)
          line(i, y, i, y + h)
        }
      })
    }
  }


  var baseHue = 0f
  def drawGradient(x:Float, y:Float) {

    val radius = dimR/2
    //var h = random(0, 360)
    //var h = baseHue
    (1 to radius).reverse.foreach(r => {
      //fill(h, 90, 90)
      fill((baseHue + r) % 360, 90, 90)
      ellipse(x, y, r, r)
      //h = (h + 1) % 360
      //h = (h + radius) % 360
      //h = (h + .001f) % 360f
    })
    baseHue += 1
  }


}
object ColourPlay {
  def main(args: Array[String]) {
    val BOOTING_CLASS_NAME = this.getClass.getName.dropRight(1)
    val appletArgs = Array(BOOTING_CLASS_NAME)
    PApplet.runSketch(appletArgs, new ColourPlay with CameraMixin)
  }
}
