package com.nekopiano.scala.processing.sandbox.poc.camera

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPVector, ThreeDimensionalCameraPApp}

/**
  * Created by neko on 07/08/2016.
  */
class ManualCameraApp extends ThreeDimensionalCameraPApp {
  override def settings(): Unit = {
    size(1024, 768, P3D)
    pixelDensity(displayDensity())
  }
  override def setup(): Unit = {
  }
  override def drawObjects(): Unit = {
    background(250)

    fill(100,100)
    box(100)
    usingMatrix {
      translate(200,250,100)
      box(200)
    }
  }

  override def drawFixedObjects(): Unit = {
    textSize(8)
    fill(10)
    text("lerpedTranslateVector=" + lerpedTranslateVector, ScalaPVector(10, height - 20))
    text("targetTranslateVector=" + targetTranslateVector, ScalaPVector(10, height - 10))
    text("lerpedRotateVector=" + lerpedRotateVector, ScalaPVector(300, height - 20))
    text("targetRotateVector=" + targetRotateVector, ScalaPVector(300, height - 10))
  }
}
object ManualCameraApp extends ScalaPAppCompanion {}
