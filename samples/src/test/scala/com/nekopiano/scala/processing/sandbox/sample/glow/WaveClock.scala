package com.nekopiano.scala.processing.sandbox.sample.glow

import com.nekopiano.scala.processing.{ScalaPAppCompanion, TwoDimensionalPApp}

/**
  * Created on 07/08/2016.
  */
class WaveClockApp extends TwoDimensionalPApp {

  // Jan 2009
  // http://www.abandonedart.org
  // http://www.zenbullets.com
  //
  // This work is licensed under a Creative Commons 3.0 License.
  // (Attribution - NonCommerical - ShareAlike)
  // http://creativecommons.org/licenses/by-nc-sa/3.0/
  //
  // This basically means, you are free to use it as long as you:
  // 1. give http://www.zenbullets.com a credit
  // 2. don't use it for commercial gain
  // 3. share anything you create with it in the same way I have
  //
  // These conditions can be waived if you want to do something groovy with it
  // though, so feel free to email me via http://www.zenbullets.com


  //================================= global vars

  var _num = 10;
  var _angnoise = 0f
  var _radiusnoise = 0f
  var _xnoise = 0f
  var _ynoise = 0f
  var _angle = -PI/2f
  var _radius = 100f
  var _strokeCol = 254f

  //================================= init

  override def settings: Unit = {
    size(500, 300);

  }

  override def setup() {
    //size(500, 300);
    smooth();
    frameRate(30);

    clearBackground();

    _angnoise = random(10);
    _radiusnoise = random(10);
    _xnoise = random(10);
    _ynoise = random(10);
  }

  def clearBackground() {
    background(255)
  }

  //================================= frame loop

  override def draw() {

    _radiusnoise += 0.005f
    _radius = (noise(_radiusnoise) * 550) +1;

    _angnoise += 0.005f
    _angle += (noise(_angnoise) * 6) - 3;
    if (_angle > 360) { _angle -= 360; }
    if (_angle < 0) { _angle += 360; }

    // wobble centre
    _xnoise += 0.01f
    _ynoise += 0.01f
    val centreX = width/2 + (noise(_xnoise) * 100) - 50;
    val centreY = height/2 + (noise(_ynoise) * 100) - 50;


    import com.nekopiano.scala.processing.Angles._

    val rad = radians(_angle);
    val x1 = centreX + (_radius * cos(rad));
    val y1 = centreY + (_radius * sin(rad));

    // opposite
    val opprad = rad + PI;
    val x2 = centreX + (_radius * cos(opprad));
    val y2 = centreY + (_radius * sin(opprad));


    noFill();
    _strokeCol += _strokeChange;
    if (_strokeCol > 254) { _strokeChange *= -1; }
    if (_strokeCol < 0) { _strokeChange *= -1; }
    stroke(_strokeCol, 60);
    strokeWeight(1);
    line(x1, y1, x2, y2);
  }

  var _strokeChange = -1


  //================================= interaction

  override def mousePressed() {
    clearBackground();
  }


}
object WaveClockApp extends ScalaPAppCompanion {}
