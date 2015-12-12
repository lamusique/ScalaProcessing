package com.nekopiano.scala.processing

/**
 * Created by neko on 2015/12/06.
 */
//case class Camera(var cameraX:Float, var cameraY:Float, var cameraZ:Float)(implicit val sp5: ScalaPApplet) {
case class Camera(var eye:ScalaPVector = ScalaPVector.origin, var center:ScalaPVector = ScalaPVector.origin, var up:ScalaPVector = ScalaPVector(0,1,0))(implicit val sp5: ScalaPApplet) {
  import sp5._

  var cameraVector = ScalaPVector.origin

//  val cameraFOV = 60 * DEG_TO_RAD
//  val cameraX = width / 2.0f
//  val cameraY = height / 2.0f
//  val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)
//
//  eye = ScalaPVector(width/2.0f, height/2.0f, cameraZ)
//  center = ScalaPVector(width/2.0f, height/2.0f, 0)

  // init perspective projection based on new dimensions
//  val cameraFOV = 60 * DEG_TO_RAD
//  val cameraX = width / 2.0f
//  val cameraY = height / 2.0f
//  val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)

  //  val cameraNear = cameraZ / 10.0f
//  val cameraFar = cameraZ * 10.0f
//  val cameraAspect = width.toFloat / height.toFloat

  // by default
  //val eye = ScalaPVector(width/2.0f, height/2.0f, cameraZ)
  // val center = ScalaPVector(width/2.0f, height/2.0f, 0)

  def initialize() = {
    val cameraFOV = 60 * DEG_TO_RAD
    val cameraY = height / 2.0f
    val cameraZ = cameraY / (Math.tan(cameraFOV / 2.0f).toFloat)

    eye = ScalaPVector(width/2.0f, height/2.0f, cameraZ)
    center = ScalaPVector(width/2.0f, height/2.0f, 0)
  }
  def camera() = sp5.camera(eye, center, up)
  def camera(eye:ScalaPVector, center:ScalaPVector, up:ScalaPVector) = sp5.camera(eye, center, up)

}
