package com.nekopiano.scala.processing

import processing.core.PVector

/**
 * Be immutable as a value object over an entity.
 * Created by neko on 2015/11/23.
 */
//class ScalaPVector(val sx:Float, val sy:Float, val sz:Float) extends PVector {
class ScalaPVector(val sx:Float, val sy:Float, val sz:Float) extends PVector {

  super.set(sx, sy, sz)

  def this(sx:Float, sy:Float) = this(sx, sy, 0)

  // accessors
  override def x:Float = sx
  def y:Float = sy
  def z:Float = sz
 // mutators not allowed
}
object ScalaPVector {
  def apply(x:Float, y:Float) = new ScalaPVector(x, y)
  def apply(x:Float, y:Float, z:Float) = new ScalaPVector(x, y, z)
  def unapply(vector:ScalaPVector) = Some((vector.x, vector.y, vector.z))
}
