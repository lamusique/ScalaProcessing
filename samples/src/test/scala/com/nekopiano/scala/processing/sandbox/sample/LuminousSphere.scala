package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing.{Angles, ScalaPAppCompanion, ThreeDimensionalPApp}
import processing.core.PImage

/**
  * Created on 28/07/2016.
  */
class LuminousSphere extends ThreeDimensionalPApp {
  //http://p5aholic.hatenablog.com/entry/2015/12/10/000000


  override def settings() {
    size(800, 600, P3D)
  }


  var img:PImage = null

  override def setup() {
    // zテストを無効化
    hint(DISABLE_DEPTH_TEST);
    // 加算合成
    blendMode(ADD);
    imageMode(CENTER);
    // 画像の生成
    img = createLight(random(0.5f, 0.8f), random(0.5f, 0.8f), random(0.5f, 0.8f))

  }

  // 光る球体の画像を生成する関数
  def createLight(rPower:Float, gPower:Float, bPower:Float):PImage = {
    val side = 200; // 1辺の大きさ
    val center = side / 2.0f // 中心座標

    // 画像を生成
    val img = createImage(side, side, RGB)

    // 画像の一つ一つのピクセルの色を設定する
    (0 until side) foreach(y=>{
      (0 until side) foreach(x=>{

        val distance = (sq(center - x) + sq(center - y)) / 50.0f
        val r = ((255 * rPower) / distance).toInt
        val g = ( (255 * gPower) / distance ).toInt
        val b = ( (255 * bPower) / distance ).toInt
        img.pixels(x + y * side) = color(r, g, b)

      })
    })

    img
  }

  var velocity = 0f
  var acceleration = .05f

  override def draw() {
    background(0, 15, 30)
    //fill(255);
    //text(frameRate, 50, 50);
    translate(width/2, height/2, 0)
    rotateX(frameCount*0.01f)
    rotateY(frameCount*0.01f)

    var lastX = 0f
    var lastY = 0f
    var lastZ = 0f
    val radius = 280f
    var s = 0f
    var t = 0f


    while (s <= 180) {
      import Angles._
      val radianS = radians(s);
      val radianT = radians(t);
      val x = radius * sin(radianS) * cos(radianT);
      val y = radius * sin(radianS) * sin(radianT);
      val z = radius * cos(radianS);

      stroke(128);
      if (lastX != 0) {
        strokeWeight(1);
        line(x, y, z, lastX, lastY, lastZ);
      }

      pushMatrix();
      // 画像の座標へ原点を移動
      translate(x, y, z);
      // 画像の向きを元に戻す
      rotateY(-frameCount*0.01f)
      rotateX(-frameCount*0.01f)
      // 画像を描画
      image(img, 0, 0);
      popMatrix();

      lastX = x;
      lastY = y;
      lastZ = z;

      s += 1
      t += velocity
    }
    velocity += acceleration;
  }

  override def mousePressed() {
    img = createLight(random(0.5f, 0.8f), random(0.5f, 0.8f), random(0.5f, 0.8f))
  }

}

object LuminousSphere extends ScalaPAppCompanion {}
