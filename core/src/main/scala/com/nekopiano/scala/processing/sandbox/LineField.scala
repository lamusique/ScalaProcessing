package com.nekopiano.scala.processing.sandbox

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppCompanion, ThreeDimensionalPApp}
import peasy.PeasyCam

/**
  * Created on 07/08/2016.
  */
class LineFieldApp extends ThreeDimensionalPApp {

  override def settings: Unit = {
    size(1024, 768, P3D)
    pixelDensity(displayDensity())
    //smooth(8)
  }

  var pCam:PeasyCam = null

  override def setup(): Unit = {
    surface.setResizable(true)

    colorMode(HSB, 360, 100, 100, 100)

    val cam = new PeasyCam(this, 100)
    cam.setMinimumDistance(55)
    cam.setMaximumDistance(5000)
    //cam.setPitchRotationMode()

    cam.lookAt(width/2, height/2, 0)

    pCam = cam
  }

  val interval = 10f

  override def draw(): Unit = {
    background(0,0,0)

    // create a matrix
    stroke(100, 200, 200, 200)
    // x
    val xSlices = (0 to (width / interval).toInt) map(_ * interval)
    xSlices foreach (x => {
      line(x, 0, 0, x, height, 0)
    })
    val ySlices = (0 to (height / interval).toInt) map(_ * interval)
    ySlices foreach (y => {
      line(0, y, 0, width, y, 0)
    })

  }

}
object LineFieldApp extends ScalaPAppCompanion {}

class LuminousLine {

}