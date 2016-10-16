package com.nekopiano.scala.processing.sandbox.sample

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPVector, ThreeDimensionalPApp, TwoDimensionalPApp}

/**
  * Created on 08/08/2016.
  */
class SymmetricDrawingApp extends TwoDimensionalPApp {
  // Symmetric Ruler

  var numVertices = 6;      // 円状に複製する数
  var weight = 3;           // 線の太さ
  //color bgColor, fillColor; // 背景色と線の色
  var bgColor = 0
  var fillColor = 0

  override def settings(): Unit = {
    // 加算合成するためレンダラはP2Dで
    size(1000, 1000, P2D);
    // 線を滑らかに
    smooth(8);

  }
  override def setup(){
    // 色指定はHSB
    colorMode(HSB, 360, 100, 100, 100);
    // 加算合成
    blendMode(ADD);
    bgColor = color(210, 80, 5);
    fillColor = color(0, 80, 40);
    background(bgColor);
  }

  override def draw(){

    usingMatrix {
      // 原点を画面中心に
      translate(width / 2, height / 2)

      // マウス座標の位置調整
      val mouseX2 = mouseX - width / 2
      val mouseY2 = mouseY - height / 2
      val pmouseX2 = pmouseX - width / 2
      val pmouseY2 = pmouseY - height / 2

      // マウスを押してるときだけ描画する
      if (mousePressed) {
        // 描画フレーム数に応じて色変化
        stroke(frameCount % 361, 80, 50, 50)
        strokeWeight(weight);

        // 円状にnumVerticesの数だけ複製
        val intervalAngle = 360f / numVertices
        import com.nekopiano.scala.processing.Angles._
        val verticesRadians = (1 to numVertices).map(n => radians(n * intervalAngle))
        verticesRadians foreach (angle => {
          usingMatrix {
            rotate(angle)
            // マウスの動き通りに描画される線
            line(mouseX2, mouseY2, pmouseX2, pmouseY2)
            // x軸に反転して描画される線
            line(-mouseX2, mouseY2, -pmouseX2, pmouseY2)
          }
        })
      }

    }

    //fill(200, 200, 200)
    fill(frameCount%361, 80, 50, 50)
    textSize(16)
    text("vertices = " + numVertices, ScalaPVector(10, height - 10))
    text("pen weight = " + weight, ScalaPVector(300, height - 10))
    //text("vertices = " + numVertices, 0, 0)
  }

  override def keyPressed(){
    // 円状に複製する数の増減
    if(key == 'q') numVertices = constrain(numVertices -1, 2, 20);
    if(key == 'w') numVertices = constrain(numVertices +1, 2, 20);
    // 線の太さの増減
    if(key == 'e') weight = constrain(weight -1, 1, 20);
    if(key == 'r') weight = constrain(weight +1, 1, 20);
    // 背景初期化
    if(key == ' ') background(bgColor);
    // 画像出力
    if(key == 's') saveFrame("img/img-####.jpg");
  }
}
object SymmetricDrawingApp extends ScalaPAppCompanion {}
