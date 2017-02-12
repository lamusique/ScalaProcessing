package com.nekopiano.scala.processing.reference

import com.nekopiano.scala.processing.{CameraMixin, ScalaPApp, ScalaPAppInstanceCompanion, ThreeDimensionalPApp}

/**
 * Created on 10/02/2017.
 */
class ScalaPReferenceApp extends ThreeDimensionalPApp {

  override def settings(): Unit = {
    size(1024, 768, P3D)
  }

}

object ScalaPReferenceApp extends ScalaPAppInstanceCompanion {
  override def instance: ScalaPApp = new ScalaPReferenceApp with CameraMixin
}
