package com.nekopiano.scala.processing.sandbox.sample.glow

import com.nekopiano.scala.processing._

/**
  * Created on 07/08/2016.
  */
class LuminousLinesPerlinNoiseApp extends TwoDimensionalPApp {

  // http://p5aholic.hatenablog.com/entry/2015/12/04/000000

  implicit val sp5:ScalaPApp = this

  val numLines = 8
  val lines = Array.fill(numLines){new LineObject()}

  override def settings() {
    //fullScreen(P3D)
    size(800, 600, P3D)

    // RetinaとかのHigh-Resディスプレイ用の処理
    pixelDensity(displayDensity());

  }

  override def setup() {
    // アンチエイリアスの質を上げる
    smooth(8);
    // HSBカラーモード
    colorMode(HSB, 360, 100, 100, 100);
    // 加算合成で発光してるっぽくみせる
    blendMode(ADD);
    initWindow();
  }

  def initWindow() {
    background(0, 0, 0);
  }

  override def draw() {
    translate(width/2, height/2, 0);

    // 線の描画と更新
    (0 until numLines) foreach (i=>{
      (0 until 2) foreach (j=>{
        lines(i).update();
        lines(i).display();
      })
    })

  }

  override def mousePressed() {
    if (mouseButton == RIGHT) {
      saveFrame("images/img-####.jpg");
    } else {
      initWindow();
    }
  }

}
object LuminousLinesPerlinNoiseApp extends ScalaPAppCompanion {}


class LineObject(implicit val sp5:ScalaPApp) {

  import sp5._

  //float x1, y1, z1;                // 座標1
  var v1 = ScalaPVector.origin
  //float x2, y2, z2;                // 座標2
  var v2 = ScalaPVector.origin

  //float sx, sy, sz;                // 線の始点
  var sv = ScalaPVector.origin

  //float sxNoise, syNoise, szNoise; // sx, sy, szのノイズ
  var vNoise = ScalaPVector.origin

  //float radius, radiusNoise;       // 半径とノイズ
  var radius = 0f
  val radiusNoise = 0f

  //float radianS, radianT;          // 球体の計算用の角度
  var radianS = 0f
  var radianT = 0f

  //float sNoise, tNoise;            // radiasSとradianTのノイズ
  var sNoise = 0f
  var tNoise = 0f

  var hue = 0

//  def this()(implicit sp5:ScalaPApp) = {
//    this()
//    // 初期化
//    //      sxNoise = random(10);
//    //      syNoise = random(10);
//    //      szNoise = random(10);
//    vNoise = ScalaPVector(random(10),random(10),random(10))
//
//    sNoise = random(10)
//    tNoise = random(10)
//
//    hue = random(360).toInt
//    radius = height / 2.0f - 50.0f
//  }

  locally {
    vNoise = ScalaPVector(random(10),random(10),random(10))

    sNoise = random(10)
    tNoise = random(10)

    hue = random(360).toInt
    radius = height / 2.0f - 50.0f
  }

  // 更新を行うメソッド
  def update() {
    // noise()で始点を変動させる
    //      sxNoise += 0.01;
    //      syNoise += 0.01;
    //      szNoise += 0.01;

    vNoise = vNoise.add(.01f, .01f, .01f)

    val sx = noise(vNoise.x) * 100 - 50;
    val sy = noise(vNoise.y) * 100 - 50;
    val sz = noise(vNoise.z) * 100 - 50;

    sv = sv.add(sx, sy, sz)

    // noise()で角度を変動させる
    sNoise += 0.003f
    tNoise += 0.003f
    radianS = noise(sNoise) * 2 * TWO_PI - TWO_PI;
    radianT = noise(tNoise) * 2 * TWO_PI - TWO_PI;

    import com.nekopiano.scala.processing.Angles._

    // 球体の面上にある座標の計算
    val x1 = radius * sin(radianS) * cos(radianT);
    val y1 = radius * sin(radianS) * sin(radianT);
    val z1 = radius * cos(radianS);

    //      x2 = - x1;
    //      y2 = - y1;
    //      z2 = - z1;

    v2 = v2.set(-v1.x, -v1.y, -v1.y)

  }

  // 描画を行うメソッド
  def display() {
    stroke(hue, 80, 8, 50);
    strokeWeight(1);
    //      line(sx, sy, sz, x1, y1, z1);
    //      line(sx, sy, sz, x2, y2, z2);
    line(sv, v1)
    line(sv, v2)

    stroke(0, 0, 100, 25);
    strokeWeight(1);
    //      point(x1, y1, z1);
    //      point(x2, y2, z2);
    point(v1)
    point(v2)
  }
}
