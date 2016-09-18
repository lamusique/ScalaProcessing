package com.nekopiano.scala.processing.sandbox.poc.opengl

import com.nekopiano.scala.processing._
import processing.opengl.PShader

/**
  * Created on 19/09/2016.
  */
class GradientSphere extends ThreeDimensionalPApp {

  // Can a gradient be applied to a sphere?
  // https://forum.processing.org/two/discussion/5305/can-a-gradient-be-applied-to-a-sphere

  var coloredSphere:PShader = null

  override def settings() {
    size(1280, 720, P3D)
  }
  override def setup() {
    coloredSphere = loadShader("opengl/coloredSphereFrag.glsl", "opengl/coloredSphereVert.glsl");
  }

  override def draw() {
    coloredSphere.set("border", map(mouseY, 0, height, -250, 250));
    shader(coloredSphere);
    background(245, 238, 184);
    lights();
    pushMatrix();
    translate(width/2, height/2);
    fill(246, 225, 65);
    sphere(250);
    popMatrix();
  }

}
object GradientSphere extends ScalaPAppInstanceCompanion {
  override def instance: ScalaPApp = new GradientSphere with CameraMixin
}
