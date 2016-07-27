package com.nekopiano.scala.processing

import processing.core.PApplet

import scala.reflect.runtime._

/**
 * Created on 2015/11/23.
 */
trait ScalaPApp extends PApplet with ScalaPConstants {
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
  def dottedLine(start: ScalaPVector, end:ScalaPVector, interval:Int = 8, stroke:Int = 2): Unit = {
    val distance = start.distance(end)
    val intervals = distance / interval

    usingStyle {
      strokeWeight(stroke)
      1 to intervals.toInt foreach (i => {
        val rate = 1 / intervals * i
        val lerped = start.lerp(end, rate)
        point(lerped)
      })
    }

  }
  def dashedLine(start: ScalaPVector, end:ScalaPVector, interval:Int = 8, stroke:Int = 2): Unit = {
    val distance = start.distance(end)
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


  // ================
  // Mathematics
  // ================
  def sqrt(n:Float) = PApplet.sqrt(n:Float)

//  def sin(angle:Float) = Angles.sin(angle)
//  def tan(angle:Float) = Angles.tan(angle)
//  def degrees(radians:Float) = Angles.degrees(radians)
//  def radians(degrees:Float) = Angles.radians(degrees)


  // ================================
  // Dimensional Methods
  // ================================

  // ================
  // Drawing
  // ================

  def text(txt:String, vector: ScalaPVector):Unit

  def point(vector:ScalaPVector): Unit

  def line(start: ScalaPVector, end:ScalaPVector): Unit



}

trait ScalaPAppCompanion {
  def main(args: Array[String]) {
    val BOOTING_CLASS_NAME = this.getClass.getName.dropRight(1)
    // This specifies the class to be instantiated.
    val appletArgs = Array(BOOTING_CLASS_NAME)
    if (args != null) {
      PApplet.main(PApplet.concat(appletArgs, args))
    } else {
      PApplet.main(appletArgs)
    }
  }
}
