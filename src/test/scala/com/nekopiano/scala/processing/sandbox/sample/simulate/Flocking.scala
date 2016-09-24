package com.nekopiano.scala.processing.sandbox.sample.simulate

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppInstanceCompanion, TwoDimensionalPApp}
import processing.core.PVector

/**
  * Created on 24/09/2016.
  */
class Flocking extends TwoDimensionalPApp {

  // https://processing.org/examples/flocking.html
  // http://harry.me/blog/2011/02/17/neat-algorithms-flocking/

  implicit val sp5 = this

  var flock: Flock = null

  override def settings() = {
    size(640, 360)
  }
  override def setup() {

    flock = new Flock
    // Add an initial set of boids into the system
    var i: Int = 0
    while (i < 150) {
      {
        flock.addBoid(new Boid(width / 2, height / 2))
      }
      {
        i += 1; i - 1
      }
    }
  }

  override def draw() {
    background(50)
    flock.run()
  }

  // Add a new boid into the System
  override def mousePressed() {
    flock.addBoid(new Boid(mouseX, mouseY))
  }
}
object Flocking extends ScalaPAppInstanceCompanion {
  override def instance = new Flocking
}

// The Flock (a list of Boid objects)
class Flock private[simulate]() {
  // Initialize the ArrayList
  var boids: Seq[Boid] = Seq.empty[Boid] // An ArrayList for all the boids
  def run() {
    import scala.collection.JavaConversions._
    for (b <- boids) {
      b.run(boids) // Passing the entire list of boids to each boid individually
    }
  }

  def addBoid(b: Boid) {
    boids = boids :+ b
  }
}

// The Boid class
class Boid private[simulate](val x: Float, val y: Float)(implicit val sp5:ScalaPApp) {

  import sp5._
  import com.nekopiano.scala.processing.Angles._

  // This is a new PVector method not yet implemented in JS
  // velocity = PVector.random2D();
  // Leaving the code temporarily this way so that this example runs in JS
  val angle: Float = random(TWO_PI)
  var location: PVector = new PVector(x, y)
  var velocity: PVector = new PVector(cos(angle), sin(angle))
  var acceleration: PVector = new PVector(0, 0)
  var r: Float = 2.0f
  var maxforce: Float = 0.03f // Maximum steering force
  var maxspeed: Float = 2.0f // Maximum speed
  def run(boids:Seq[Boid]) {
    flock(boids)
    update()
    borders()
    render()
  }


  def applyForce(force: PVector) {
    // We could add mass here if we want A = F / M
    acceleration.add(force)
  }

  // We accumulate a new acceleration each time based on three rules
  def flock(boids:Seq[Boid]) {
    val sep: PVector = separate(boids) // Separation
    val ali: PVector = align(boids) // Alignment
    val coh: PVector = cohesion(boids) // Cohesion
    // Arbitrarily weight these forces
    sep.mult(1.5f)
    ali.mult(1.0f)
    coh.mult(1.0f)
    // Add the force vectors to acceleration
    applyForce(sep)
    applyForce(ali)
    applyForce(coh)
  }

  // Method to update location
  def update() {
    // Update velocity
    velocity.add(acceleration)
    // Limit speed
    velocity.limit(maxspeed)
    location.add(velocity)
    // Reset accelertion to 0 each cycle
    acceleration.mult(0)
  }

  // A method that calculates and applies a steering force towards a target
  // STEER = DESIRED MINUS VELOCITY
  def seek(target: PVector): PVector = {
    val desired: PVector = PVector.sub(target, location) // A vector pointing from the location to the target
    // Scale to maximum speed
    desired.normalize
    desired.mult(maxspeed)
    // Above two lines of code below could be condensed with new PVector setMag() method
    // Not using this method until Processing.js catches up
    // desired.setMag(maxspeed);
    // Steering = Desired minus Velocity
    val steer: PVector = PVector.sub(desired, velocity)
    steer.limit(maxforce) // Limit to maximum steering force
    steer
  }

  def render() {
    // Draw a triangle rotated in the direction of velocity
    val theta: Float = velocity.heading2D + radians(90)
    // heading2D() above is now heading() but leaving old syntax until Processing.js catches up
    fill(200, 100)
    stroke(255)
    pushMatrix
    translate(location.x, location.y)
    rotate(theta)
    beginShape(TRIANGLES)
    vertex(0, -r * 2)
    vertex(-r, r * 2)
    vertex(r, r * 2)
    endShape
    popMatrix
  }

  // Wraparound
  def borders() {
    if (location.x < -r) location.x = width + r
    if (location.y < -r) location.y = height + r
    if (location.x > width + r) location.x = -r
    if (location.y > height + r) location.y = -r
  }

  // Separation
  // Method checks for nearby boids and steers away
  def separate(boids: Seq[Boid]): PVector = {
    val desiredseparation: Float = 25.0f
    val steer: PVector = new PVector(0, 0, 0)
    var count: Int = 0
    // For every boid in the system, check if it's too close
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d: Float = PVector.dist(location, other.location)
      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
      if ((d > 0) && (d < desiredseparation)) {
        // Calculate vector pointing away from neighbor
        val diff: PVector = PVector.sub(location, other.location)
        diff.normalize
        diff.div(d) // Weight by distance
        steer.add(diff)
        count += 1 // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) steer.div(count.toFloat)
    // As long as the vector is greater than 0
    if (steer.mag > 0) {
      // First two lines of code below could be condensed with new PVector setMag() method
      // Not using this method until Processing.js catches up
      // steer.setMag(maxspeed);
      // Implement Reynolds: Steering = Desired - Velocity
      steer.normalize
      steer.mult(maxspeed)
      steer.sub(velocity)
      steer.limit(maxforce)
    }
    steer
  }

  // Alignment
  // For every nearby boid in the system, calculate the average velocity
  def align(boids: Seq[Boid]): PVector = {
    val neighbordist: Float = 50
    val sum: PVector = new PVector(0, 0)
    var count: Int = 0
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d: Float = PVector.dist(location, other.location)
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.velocity)
        count += 1
      }
    }
    if (count > 0) {
      sum.div(count.toFloat)
      // First two lines of code below could be condensed with new PVector setMag() method
      // Not using this method until Processing.js catches up
      // sum.setMag(maxspeed);
      // Implement Reynolds: Steering = Desired - Velocity
      sum.normalize
      sum.mult(maxspeed)
      val steer: PVector = PVector.sub(sum, velocity)
      steer.limit(maxforce)
      steer
    }
    else new PVector(0, 0)
  }

  // Cohesion
  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
  def cohesion(boids: Seq[Boid]): PVector = {
    val neighbordist: Float = 50
    val sum: PVector = new PVector(0, 0) // Start with empty vector to accumulate all locations
    var count: Int = 0
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d: Float = PVector.dist(location, other.location)
      if ((d > 0) && (d < neighbordist)) {
        sum.add(other.location) // Add location
        count += 1
      }
    }
    if (count > 0) {
      sum.div(count)
      seek(sum) // Steer towards the location
    }
    else new PVector(0, 0)
  }
}
