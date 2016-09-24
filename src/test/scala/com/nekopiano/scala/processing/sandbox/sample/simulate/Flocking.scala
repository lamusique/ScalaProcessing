package com.nekopiano.scala.processing.sandbox.sample.simulate

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppInstanceCompanion, ScalaPVector, TwoDimensionalPApp}

/**
  * Created on 24/09/2016.
  */
class Flocking extends TwoDimensionalPApp {

  // https://processing.org/examples/flocking.html
  // http://harry.me/blog/2011/02/17/neat-algorithms-flocking/

  implicit val sp5 = this

  val flock: Flock = new Flock

  override def settings() = {
    //size(640, 360)
    size(1280, 768)
  }
  override def setup() {

    // Add an initial set of boids into the system
    (0 until 150) foreach( i =>
      flock.addBoid(new Boid(width / 2, height / 2))
      )

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
class Flock () {
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
class Boid (val x: Float, val y: Float)(implicit val sp5:TwoDimensionalPApp) {

  import sp5._
  import com.nekopiano.scala.processing.Angles._

  // This is a new PVector method not yet implemented in JS
  // velocity = PVector.random2D();
  // Leaving the code temporarily this way so that this example runs in JS
  var location: ScalaPVector = ScalaPVector(x, y)
  var velocity: ScalaPVector = {
    val angle: Float = random(TWO_PI)
    ScalaPVector(cos(angle), sin(angle))}
  var acceleration: ScalaPVector = ScalaPVector(0, 0)
  var r: Float = 2.0f
  var maxforce: Float = 0.03f // Maximum steering force
  var maxspeed: Float = 2.0f // Maximum speed


  def run(boids:Seq[Boid]) {
    flock(boids)
    update()
    this.location = borders(this.location)
    render()
  }


  def applyForce(force: ScalaPVector) {
    // We could add mass here if we want A = F / M
    this.acceleration = this.acceleration.add(force)
  }

  // We accumulate a new acceleration each time based on three rules
  def flock(boids:Seq[Boid]) {
    // Arbitrarily weight these forces
    // Separation
    val sep = separate(boids).mult(1.5f)
    // Alignment
    val ali = align(boids).mult(1.0f)
    // Cohesion
    val coh = cohesion(boids).mult(1.0f)

    // Add the force vectors to acceleration
    applyForce(sep)
    applyForce(ali)
    applyForce(coh)
  }

  // Method to update location
  def update() {
    // Update velocity
    // Limit speed
    this.velocity = this.velocity.add(acceleration).limit(maxspeed)
    this.location = this.location.add(this.velocity)
    // Reset accelertion to 0 each cycle
    this.acceleration = this.acceleration.mult(0)
  }

  // A method that calculates and applies a steering force towards a target
  // STEER = DESIRED MINUS VELOCITY
  def seek(target: ScalaPVector): ScalaPVector = {

    // A vector pointing from the location to the target
    // Scale to maximum speed
    val desired = target.sub(location).normalize.mult(maxspeed)

    // Above two lines of code below could be condensed with ScalaPVector setMag() method
    // Not using this method until Processing.js catches up
    // desired.setMag(maxspeed);
    // Steering = Desired minus Velocity
    // Limit to maximum steering force
    val steer = desired.sub(velocity).limit(maxforce)
    steer
  }

  def render() {
    // Draw a triangle rotated in the direction of velocity
    val theta = velocity.heading + radians(90)
    // heading2D() above is now heading() but leaving old syntax until Processing.js catches up
    fill(200, 100)
    stroke(255)
    usingMatrix {
      translate(this.location)
      rotate(theta)
      beginShape(TRIANGLES)
      vertex(0, -r * 2)
      vertex(-r, r * 2)
      vertex(r, r * 2)
      endShape
    }
  }

  // Wraparound
  def borders(location:ScalaPVector) = {

    val x = if (location.x < -r) width + r
    else if (location.x > width + r) -r
    else location.x

    val y = if (location.y < -r) height + r
    else if (location.y > height + r) -r
    else location.y

    ScalaPVector(x, y)
  }

  // Separation
  // Method checks for nearby boids and steers away
  def separate(boids: Seq[Boid]): ScalaPVector = {
    val desiredseparation: Float = 25.0f
    var steer = ScalaPVector(0, 0, 0)
    var count: Int = 0
    // For every boid in the system, check if it's too close
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d = location.dist(other.location)
      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
      if ((d > 0) && (d < desiredseparation)) {
        // Calculate vector pointing away from neighbor
        // Weight by distance
        val diff = location.sub(other.location).normalize.div(d)
        steer = steer.add(diff)
        count += 1 // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) steer = steer.div(count.toFloat)
    // As long as the vector is greater than 0
    if (steer.mag > 0) {
      // First two lines of code below could be condensed with ScalaPVector setMag() method
      // Not using this method until Processing.js catches up
      // steer.setMag(maxspeed);
      // Implement Reynolds: Steering = Desired - Velocity
      steer = steer.normalize.mult(maxspeed).sub(velocity).limit(maxforce)
    }
    steer
  }

  // Alignment
  // For every nearby boid in the system, calculate the average velocity
  def align(boids: Seq[Boid]): ScalaPVector = {
    val neighbordist: Float = 50
    var sum: ScalaPVector = ScalaPVector(0, 0)
    var count: Int = 0
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d = location.dist(other.location)
      if ((d > 0) && (d < neighbordist)) {
        sum = sum.add(other.velocity)
        count += 1
      }
    }
    if (count > 0) {
      sum = sum.div(count.toFloat).normalize.mult(maxspeed)
      // First two lines of code below could be condensed with ScalaPVector setMag() method
      // Not using this method until Processing.js catches up
      // sum.setMag(maxspeed);
      // Implement Reynolds: Steering = Desired - Velocity

      val steer = sum.sub(velocity).limit(maxforce)
      steer
    }
    else ScalaPVector(0, 0)
  }

  // Cohesion
  // For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
  def cohesion(boids: Seq[Boid]): ScalaPVector = {
    val neighbordist: Float = 50
    // Start with empty vector to accumulate all locations
    var sum = ScalaPVector(0, 0)
    var count: Int = 0
    import scala.collection.JavaConversions._
    for (other <- boids) {
      val d = location.dist(other.location)
      if ((d > 0) && (d < neighbordist)) {
        sum = sum.add(other.location) // Add location
        count += 1
      }
    }
    if (count > 0) {
      sum = sum.div(count)
      seek(sum) // Steer towards the location
    }
    else ScalaPVector(0, 0)
  }
}
