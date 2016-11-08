package com.nekopiano.scala.processing.sandbox.sample.glow

import com.nekopiano.scala.processing.{ScalaPAppCompanion, ScalaPVector, TwoDimensionalPApp}

/**
  * Created on 07/08/2016.
  */
class LuminousLinesNoisedTargetsApp extends TwoDimensionalPApp {
  val numLines = 6;

  var lines = Array.empty[Line]

  override def settings(): Unit = {
    //fullScreen(P3D);
    size(800, 600, P3D)
    pixelDensity(displayDensity());

  }

  override def setup() {
    smooth(8);
    colorMode(HSB, 360, 100, 100, 100);
    blendMode(ADD);
    initWindow();

    // after height defined
    lines = Array.fill(numLines){new Line()}
  }

  def initWindow() {
    background(0, 0, 0)
  }

  override def draw() {
    translate(width/2, height/2, 0)

//    for (int i = 0; i < numLines; i++) {
//      for (int j = 0; j < 2; j++) {
//        lines[i].update();
//        lines[i].display();
//      }
//    }

    lines.foreach(line => {
      line.update()
      line.display()
      // to adjust the speed
      line.update()
      line.display()
    })

  }

  override def mousePressed() {
    if (mouseButton == RIGHT) {
      saveFrame("images/img-####.jpg");
    } else {
      initWindow();
    }
  }

  class Line {

    //float x1, y1, z1;                // 座標1
    var v1 = ScalaPVector.origin
    //float x2, y2, z2;                // 座標2
    var v2 = ScalaPVector.origin
    //float targetX, targetY, targetZ; // 座標1,2の到達目標点
    var targetV = ScalaPVector.origin
    // start
    var sv = ScalaPVector.origin

    var radius = 0f

    //float radianS, radianT;          // 球体の計算用の角度
    var radianS = 0f
    var radianT = 0f

    var hue = 0

    locally {
      hue = random(360).toInt
      radius = height / 2.0f
      setTarget()
    }

    // targetX, Y, Zの設定
    def setTarget() {
      // targetX, Y, Zを球体の面上のランダムな座標に設定
//      val sx = random(-50, 50);
//      val sy = random(-50, 50);
//      val sz = random(-50, 50);
      sv = sv.set(random(-50, 50), random(-50, 50), random(-50, 50))

      radianS = random(-TWO_PI, TWO_PI);
      radianT = random(-TWO_PI, TWO_PI);

      import com.nekopiano.scala.processing.Angles._

      val targetX = radius * sin(radianS) * cos(radianT);
      val targetY = radius * sin(radianS) * sin(radianT);
      val targetZ = radius * cos(radianS);

      targetV = targetV.set(targetX, targetY, targetZ)
    }

    def update() {
      // 一定確率でtargetを設定
      if (random(100) < 5) {
        setTarget();
      }

      // targetに向かって座標をイージングで動かす
//      val x1 = (targetV.x - v1.x) * 0.04f
//      val y1 = (targetV.y - v1.y) * 0.04f
//      val z1 = (targetV.z - v1.z) * 0.04f
      val x1 = v1.x + (targetV.x - v1.x) * 0.04f
      val y1 = v1.y + (targetV.y - v1.y) * 0.04f
      val z1 = v1.z + (targetV.z - v1.z) * 0.04f

      //v1 = v1.add(x1, y1, z1)
      v1 = v1.set(x1, y1, z1)

      v2 = v2.set(-v1.x, -v1.y, -v1.z)
    }

    def display() {
      stroke(hue, 80, 8, 50);
      strokeWeight(1);
      line(sv, v1)
      line(sv, v2)

      stroke(0, 0, 100, 25);
      strokeWeight(1);
      point(v1)
      point(v2)
    }
  }
}
object LuminousLinesNoisedTargetsApp extends ScalaPAppCompanion {}
