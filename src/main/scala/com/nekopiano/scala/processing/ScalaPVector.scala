package com.nekopiano.scala.processing

import processing.core.PVector

/**
 * Be immutable as a value object over an entity.
 * Created by neko on 2015/11/23.
 */
//class ScalaPVector(val sx:Float, val sy:Float, val sz:Float) extends PVector {
case class ScalaPVector(var x:Float, var y:Float, var z:Float = 0) {
  private val pVector = new PVector(x, y, z)

  def setZ(settingZ:Float) = ScalaPVector(this.x, this.y, settingZ)

  // ================
  // Calculation
  // ================
  def angleWith(vector: ScalaPVector):Float = PVector.angleBetween(this.pVector, vector.pVector)
  def lerp(anotherVector: ScalaPVector, amount:Float = 0.5f):ScalaPVector = ScalaPVector(PVector.lerp(this.pVector, anotherVector.pVector, amount))
  def distance(anotherVector: ScalaPVector):Float = this.pVector.dist(anotherVector.pVector)

}
object ScalaPVector {
  def apply(pVector: PVector):ScalaPVector = ScalaPVector(pVector.x, pVector.y, pVector.z)
}