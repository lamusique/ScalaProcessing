package com.nekopiano.scala.processing.sandbox.poc.glow

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppCompanion, ScalaPApplet, ThreeDimensionalPApp}
import processing.core.{PGraphics, PImage, PVector}
import processing.opengl.{PGL, PGraphicsOpenGL}
//import javax.media.opengl.*

/**
  * Glow w/ OpenGL
  *
  * Created on 28/07/2016.
  */
class GlowOGLApp extends ThreeDimensionalPApp {
  // http://takuma-art.blogspot.jp/2009/08/processingglow.htm
  // http://log.nissuk.info/2012/02/processing-20a4-glow-openglapplet.html

  var pgl:PGraphicsOpenGL = null

  override def settings() {
    // P2D and P3D is based on OpenGL
    size(600, 400, P2D)
    //size(600, 400, P3D)

    // 2. hintを設定します。
    //   - ENABLE_OPENGL_4X_SMOOTH: アンチエイリアス有効
    //   - DISABLE_OPENGL_ERROR_REPORT: エラー処理無効
    //   参考: http://www.technotype.net/processing/reference/hint_.html
    // anti alias
//    hint(ENABLE_OPENGL_4X_SMOOTH)
//    hint(DISABLE_OPENGL_ERROR_REPORT);

    smooth()

  }

  override def setup() {
    background(10)

    // 3. PGrahicsOpenGLを取得します。
    pgl = super.getGraphics().asInstanceOf[PGraphicsOpenGL]

    img = setupImage(250, 250)

  }

  var img:PGraphics = null

  def setupImage(w:Int, h:Int) = {
    img = createGraphics(w, h, P2D)
    //img = createGraphics(w, h, P3D)
    val r = img.width / 2f
    img.beginDraw()
    img.noStroke()
    img.smooth()
    img.colorMode(HSB, 360, 100, 100, 100)
    val steps = 2
    (steps to 1).foreach(i => {
      img.fill(0, 0, 100, 100 / i)
      img.ellipse(img.width / 2, img.height / 2, r * i/steps, r * i/steps);
    })
    img.filter(BLUR, r / 10)
    img.endDraw()
    img
  }

  var prevMouseX:Int = 0
  var prevMouseY:Int = 0
  var aa:Float = 0
  var ss:Float = 0

  var hueValue = 0

  override def draw() {

    fill(250)
    box(100)
    rect(10, 20, 30, 40)

    val distance = sqrt(pow(mouseX - prevMouseX, 2) + pow(mouseY - prevMouseY, 2))
    aa = max(50, min(aa + distance / 10, 100))
    ss = max(0, min(ss + distance / 10, 50))

    val a = 100 - aa
    val s = (100 - ss) / 100

    fill(0, 0, 0, a);
    rect(0, 0, width, height);
    drawImage(mouseX, mouseY, 2 * s);
    drawImage(mouseX, mouseY, 1.5f * s)
    drawImage(mouseX, mouseY, 1 * s)
    drawImage(mouseX, mouseY, 0.5f * s)

    hueValue = (hueValue + 1) % 360;
    prevMouseX = mouseX;
    prevMouseY = mouseY;

    aa = if (0 < aa) aa -1 else 0
    ss = if (0 < ss) ss -1 else 0

  }

  def drawImage(x:Int, y:Int, s:Float) {
    // 4. PGrahicsOpenGLからGLを取得、
    //    アルファブレンディング(GL_BLEND)
    //    加算合成(GL_SRC_ALPHA, GL_ONE)を有効にして画像を描画します
    val gl = pgl.beginPGL()
    gl.enable(PGL.BLEND)
    gl.blendFunc(PGL.SRC_ALPHA, PGL.ONE)
    colorMode(HSB, 360, 100, 100, 100)
    imageMode(CENTER)
    tint(hueValue, 50, 100, 100)
    image(img, x, y, img.width * s, img.height * s)
    pgl.endPGL()
  }
}


object GlowOGLApp extends ScalaPAppCompanion {

}
//
//case class Star(starImg:PImage, pos:Point, vel:PVector, w:Float, h:Float)(implicit val sp5:ThreeDimensionalPApp) {
//  import sp5._
//
//  def display() {
//    pushMatrix()
//    translate(0, 0, pos.z)
//    image(starImg, pos.x-w/2, pos.y-h/2, w, h)
//    popMatrix()
//  }
//
//
//  def move(){
//    pos.x+=random(-5,5);
//    pos.y+=random(-5,5);
//    pos.z+=random(-10,10);
//  }
//
//  def bounce(){
//    if(pos.x+w/2>width || pos.x-w/2<0){
//      vel.x*= -1;
//      if(pos.x+w/2>width) pos.x = width-w/2
//      else pos.x = w/2
//    }
//    if(pos.y+h/2>height || pos.y-h/2<0){
//      vel.y*= -1;
//      if(pos.y+h/2>height) pos.y = height-h/2
//      else pos.y = h/2
//    }
//  }
//
//}
