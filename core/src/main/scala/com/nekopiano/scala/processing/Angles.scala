package com.nekopiano.scala.processing

import processing.core.PApplet

/**
 * Created on 2015/12/06.
 */
case class Angles(x:Float = 0f, y:Float = 0f, z:Float = 0f) extends ScalaPConstants {

  def xDegrees = Angles.degrees(x)
  def yDegrees = Angles.degrees(y)
  def zDegrees = Angles.degrees(z)

  def xPiRadians = "π・ " + x / PI
  def yPiRadians = "π・ " + y / PI
  def zPiRadians = "π・ " + z / PI


  // ================
  // Operation
  // ================
  def setX(settingX:Float) = Angles(settingX, this.y, this.z)
  def setY(settingY:Float) = Angles(this.x, settingY, this.z)
  def setZ(settingZ:Float) = Angles(this.x, this.y, settingZ)

  def addX(settingX:Float) = Angles(this.x + settingX, this.y, this.z)
  def addY(settingY:Float) = Angles(this.x, this.y + settingY, this.z)
  def addZ(settingZ:Float) = Angles(this.x, this.y, this.z + settingZ)

  def add(another:Angles) = {
    Angles(this.x + another.x,
    this.y + another.y,
    this.z + another.z)
  }


}
object Angles {
  val origin = Angles(0f, 0f, 0f)
  // trigonometric functions
  def sin(angle:Float) = PApplet.sin(angle)
  def cos(angle:Float) = PApplet.cos(angle)
  def tan(angle:Float) = PApplet.tan(angle)
  def atan2(y:Float, x:Float) = PApplet.atan2(y, x)
  def degrees(radians:Float) = PApplet.degrees(radians)
  def radians(degrees:Float) = PApplet.radians(degrees)
}
