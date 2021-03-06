package com.nekopiano.scala.processing

import processing.core.PVector

/**
 * Be immutable as a value object over an entity.
 * Created on 2015/11/23.
 */
//class ScalaPVector(val sx:Float, val sy:Float, val sz:Float) extends PVector {
//case class ScalaPVector(var x:Float, var y:Float, var z:Float = 0) {
case class ScalaPVector(val x:Float, val y:Float, val z:Float = 0) {
  private val pVector = new PVector(x, y, z)

  // ================
  // Coordination
  // ================
  // Coordination methods need Graphics here.

  // ================
  // Operation
  // ================
  def set(settingX:Float=this.x, settingY:Float=this.y, settingZ:Float=this.z) = ScalaPVector(settingX, settingY, settingZ)
  def setX(settingX:Float) = ScalaPVector(settingX, this.y, this.z)
  def setY(settingY:Float) = ScalaPVector(this.x, settingY, this.z)
  def setZ(settingZ:Float) = ScalaPVector(this.x, this.y, settingZ)

  def add(settingX:Float, settingY:Float, settingZ:Float) = ScalaPVector(this.x + settingX, this.y + settingY, this.z + settingZ)
  def addX(settingX:Float) = ScalaPVector(this.x + settingX, this.y, this.z)
  def addY(settingY:Float) = ScalaPVector(this.x, this.y + settingY, this.z)
  def addZ(settingZ:Float) = ScalaPVector(this.x, this.y, this.z + settingZ)

  // Object to Object
  def add(vector: ScalaPVector) = ScalaPVector(this.x + vector.x, this.y + vector.y, this.z + vector.z)
  def multiply(vector: ScalaPVector) = ScalaPVector(this.x * vector.x, this.y * vector.y, this.z * vector.z)

  // ================
  // Calculation
  // ================
  def angleWith(vector: ScalaPVector):Float = PVector.angleBetween(this.pVector, vector.pVector)
  def lerp(anotherVector: ScalaPVector, amount:Float = 0.5f):ScalaPVector = ScalaPVector(PVector.lerp(this.pVector, anotherVector.pVector, amount))
  def dist(anotherVector: ScalaPVector):Float = this.pVector.dist(anotherVector.pVector)
  def mult(n:Float) = ScalaPVector(this.pVector.mult(n:Float))
  def div(n:Float) = ScalaPVector(pVector.div(n:Float))
  def sub(v:ScalaPVector) = {
    val substracted = this.pVector.sub(v.pVector)
    ScalaPVector(substracted)
  }
  def limit(max:Float) = ScalaPVector(this.pVector.limit(max:Float))
  def normalize = ScalaPVector(this.pVector.normalize())
  def mag = this.pVector.mag()
  def heading = this.pVector.heading()
}
object ScalaPVector {
  val origin = ScalaPVector(0, 0, 0)
  def apply(pVector: PVector):ScalaPVector = ScalaPVector(pVector.x, pVector.y, pVector.z)
}
