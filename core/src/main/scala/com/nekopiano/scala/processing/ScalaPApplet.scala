package com.nekopiano.scala.processing

import processing.core.PApplet
import processing.opengl.PGraphics2D

import scala.reflect.runtime._

/**
 * Created on 2015/11/23.
 */
@deprecated("Use separate traits for each dimension, e.g. line() works different in dimension.")
class ScalaPApplet extends PApplet with ScalaPConstants {
  import processing.core.PApplet._

  // ================
  // Utilities
  // ================

  // decouple the matrix using loan pattern
  def usingMatrix[R](f: => R):R = {
    pushMatrix()
    val ret = f
    popMatrix()
    ret
  }
  // decouple the matrix using loan pattern
  def usingStyle[R](f: => R):R = {
    pushStyle()
    val ret = f
    popStyle()
    ret
  }

  private def getFieldValueReflectively(instance: AnyRef, fieldName: String) = {
    val m = universe.runtimeMirror(instance.getClass.getClassLoader)

    println(instance.getClass.getTypeName)
    val cs = m.classSymbol(instance.getClass)
    val members = cs.toType.members

    val targetSymbol = members.find(member => {
      val name = member.name.toString
      !member.isMethod && name == fieldName
    })

    val im = m.reflect(instance)
    val targetMember = targetSymbol.get.asTerm
    val fieldMirror = im.reflectField(targetMember)
    fieldMirror.get
  }

  // ================
  // Inner Values
  // ================
  def getCamera() = {
    getFieldValueReflectively(getGraphics, "camera")
  }

  // ================
  // Coordination
  // ================
  def screen(vector: ScalaPVector) =
    ScalaPVector(screenX(vector.x, vector.y, vector.z), screenY(vector.x, vector.y, vector.z), screenZ(vector.x, vector.y, vector.z))
  def model(vector: ScalaPVector) =
    ScalaPVector(modelX(vector.x, vector.y, vector.z), modelY(vector.x, vector.y, vector.z), modelZ(vector.x, vector.y, vector.z))
  def translate(vector: ScalaPVector):Unit = translate(vector.x, vector.y, vector.z)
  def getMouseVector() = ScalaPVector(mouseX, mouseY)

  // ================
  // Viewing
  // ================
  def camera(eye:ScalaPVector, center:ScalaPVector, up:ScalaPVector = ScalaPVector(0,1,0)): Unit = {
    camera(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z)
  }

  // ================
  // Drawing
  // ================

  def text(txt:String, vector: ScalaPVector):Unit = {
    text(txt, vector.x, vector.y, vector.z)
  }

  def point(vector:ScalaPVector): Unit = {
    point(vector.x, vector.y, vector.z)
  }

  def line(start: ScalaPVector, end:ScalaPVector): Unit = {
    line(start.x, start.y, start.z, end.x, end.y, end.z)
  }
  def dottedLine(start: ScalaPVector, end:ScalaPVector, interval:Int = 8, stroke:Int = 2): Unit = {
    val distance = start.dist(end)
    val intervals = distance / interval

    usingStyle {
      strokeWeight(stroke)
      1 to intervals.toInt foreach (i => {
        val rate = 1 / intervals * i
        val lerped = start.lerp(end, rate)
        point(lerped.x, lerped.y, lerped.z)
      })
    }

  }
  def dashedLine(start: ScalaPVector, end:ScalaPVector, interval:Int = 8, stroke:Int = 2): Unit = {
    val distance = start.dist(end)
    val intervals = distance / interval

    val pairs = (1 to intervals.toInt).grouped(2).filter(_.size > 1)

    usingStyle {
      strokeWeight(stroke)
      pairs foreach (pair => {

        def lerpedVector(number: Int) = {
          val rate = 1 / intervals * number
          start.lerp(end, rate)
        }

        val startVector = lerpedVector(pair(0))
        val endVector = lerpedVector(pair(1))

        line(startVector, endVector)
      })
    }

  }

  def box(vector: ScalaPVector): Unit = {
    box(vector.x, vector.y, vector.z)
  }

  // ================
  // Mathematics
  // ================
  def sqrt(n:Float) = PApplet.sqrt(n:Float)

//  def sin(angle:Float) = Angles.sin(angle)
//  def tan(angle:Float) = Angles.tan(angle)
//  def degrees(radians:Float) = Angles.degrees(radians)
//  def radians(degrees:Float) = Angles.radians(degrees)

}
@deprecated("Use separate traits for each dimension, e.g. line() works different in dimension.")
object ScalaPApplet {
  def main(args : Array[String]) = PApplet.main(args)
}
