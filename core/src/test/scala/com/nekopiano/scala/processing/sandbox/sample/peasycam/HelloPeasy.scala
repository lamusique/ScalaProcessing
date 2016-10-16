package com.nekopiano.scala.processing.sandbox.sample.peasycam

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ThreeDimensionalPApp}
import peasy.PeasyCam

/**
  * Created by neko on 29/07/2016.
  */
class HelloPeasy extends ThreeDimensionalPApp {

  override def settings(): Unit = {
    size(200,200,P3D)

  }

  var cam:PeasyCam = null

  override def setup() {
    cam = new PeasyCam(this, 100);
    cam.setMinimumDistance(50);
    cam.setMaximumDistance(500);
  }

  override def draw() {
    rotateX(-.5f);
    rotateY(-.5f);
    background(0);
    fill(255,0,0);
    box(30);
    pushMatrix();
    translate(0,0,20);
    fill(0,0,255);
    box(5);
    popMatrix();

  }

}

object HelloPeasy extends ScalaPAppCompanion {}
