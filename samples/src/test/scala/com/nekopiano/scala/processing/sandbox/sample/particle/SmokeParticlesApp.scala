package com.nekopiano.scala.processing.sandbox.sample.particle

import java.util

import com.nekopiano.scala.processing.{CameraMixin, ScalaPApp, ScalaPAppInstanceCompanion, ThreeDimensionalPApp}
import processing.core
import processing.core.{PImage, PVector}

/**
  * Created on 19/09/2016.
  */
class SmokeParticlesApp extends ThreeDimensionalPApp {

  implicit val sp5 = this

  var ps:ParticleSystem = null

  override def settings() = {
    //size(640,360)
    size(1280, 720, P3D)
  }

  override def setup() {
    //PImage img = loadImage("texture.png");
    //val img = loadImage("texture.png")
    ps = new ParticleSystem(0,new PVector(width/2f, height-60f))
  }

  override def draw() {
    background(0);

    // Calculate a "wind" force based on mouse horizontal position
    val dx = map(mouseX,0,width,-0.2f,0.2f);
    val wind = new PVector(dx,0);
    ps.applyForce(wind);
    ps.run()

    (0 until 2) foreach(i => ps.addParticle())


    // Draw an arrow representing the wind force
    drawVector(wind, new PVector(width/2,50,0),500);

  }

  // Renders a vector object 'v' as an arrow and a location 'loc'
  def drawVector(v:PVector, loc:PVector, scayl:Float) {
    pushMatrix()
    val arrowsize = 4f
    // Translate to location to render vector
    translate(loc.x,loc.y);
    stroke(255);
    // Call vector heading function to get direction (note that pointing up is a heading of 0) and rotate
    rotate(v.heading());
    // Calculate length of vector & scale it to be bigger or smaller if necessary
    val len = v.mag()*scayl
    // Draw three lines to make an arrow (draw pointing up since we've rotate to the proper direction)
    line(0,0,len,0);
    line(len,0,len-arrowsize,+arrowsize/2);
    line(len,0,len-arrowsize,-arrowsize/2);
    popMatrix();
  }
}
object SmokeParticlesApp extends ScalaPAppInstanceCompanion {
  override def instance: ScalaPApp = new SmokeParticlesApp with CameraMixin
}



// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles

class ParticleSystem(num:Int, v:PVector)(implicit val sp5:ScalaPApp) {
  import sp5._

  // An arraylist for all the particles
  var particles:Seq[Particle] = null
  // An origin point for where particles are birthed
  var origin:PVector = null

  //lazy val img = createLight(Colors.RED)
  lazy val img = createLight(Colors.RED, 32)
  //lazy val img = loadImage("texture.png")

  //this(num:Int, v:PVector, img_:core.PImage) {
  locally {
    particles = Seq.empty[Particle]
    // Store the origin point
    origin = v.get()
      (0 until num) foreach (i=> {
        // Add "num" amount of particles to the arraylist
        particles = particles :+ new Particle(origin, img)
      })
    }

  def createLight(colors: Colors, size:Int = 150): PImage = {
    val center = size / 2.0f
    val img = createImage(size, size, RGB)
    val alphaImage = createImage(size, size, RGB)

    (0 until size) foreach (y => {
      (0 until size) foreach (x => {
        val distance = (sq(center - x) + sq(center - y)) / 10f

        val c = colors.calculate(distance)
        img.pixels(x + y * size) = c

        val alphaC = Colors.WHITE.calculate(distance)
        alphaImage.pixels(x + y * size) = alphaC

      })
    })

    img.mask(alphaImage)

    img
  }

  def run() {
    particles foreach(_.run())
    particles = particles filterNot(_.isDead)
  }

  // Method to add a force vector to all particles currently in the system
  def applyForce(dir:PVector) {
    // Enhanced loop!!!
      particles.foreach(_.applyForce(dir))
  }

  def addParticle() {
    particles = particles :+ new Particle(origin,img)
  }

}


object Colors {

  case object RED extends Colors(8, 4, 4)

  case object ORANGE extends Colors(8, 6, 4)

  case object YELLOW extends Colors(8, 8, 4)

  case object LEAF extends Colors(6, 8, 4)

  case object GREEN extends Colors(4, 8, 4)

  case object EMERALD extends Colors(4, 8, 6)

  case object CYAN extends Colors(4, 8, 8)

  case object SKY extends Colors(4, 6, 8)

  case object BLUE extends Colors(4, 4, 8)

  case object PURPLE extends Colors(6, 4, 8)

  case object MAGENTA extends Colors(8, 4, 8)

  case object WHITE extends Colors(1, 1, 1)

  val values = Array(RED, ORANGE, YELLOW, LEAF, GREEN, EMERALD, CYAN, SKY, BLUE, PURPLE, MAGENTA, WHITE)

  val SUPPRESS = 3f

  def color(a: Float, distance: Float): Int = {
    val color = (256 * a / distance - SUPPRESS).toInt
    return Math.max(0, Math.min(color, 255))
  }
}

sealed abstract class Colors(r: Float, g: Float, b: Float) {

  def calculate(d: Float): Int = 0xff << 24 | Colors.color(r, d) << 16 | Colors.color(g, d) << 8 | Colors.color(b, d)

}




// A simple Particle class, renders the particle as an image
class Particle(l:PVector, img:PImage)(implicit val sp5:ScalaPApp) {
  import sp5._


  var loc:PVector = l.copy()
  var vel:PVector =  {
    val vx = randomGaussian() * 0.3f
    val vy = randomGaussian() * 0.3f - 1.0f
//    val vx = randomGaussian()
//    val vy = randomGaussian() - 1.0f
    new PVector(vx,vy)
  }
  var acc:PVector = new PVector(0,0)
  var lifespan = 100f
  //var lifespan = 400f


  def run() {
    update()
    render()
  }

  // Method to apply a force vector to the Particle object
  // Note we are ignoring "mass" here
  def applyForce(f:PVector) {
    acc.add(f)
  }

  // Method to update location
  def update() {
    vel.add(acc);
    loc.add(vel);
    lifespan -= 2.5f
    acc.mult(0); // clear Acceleration
  }

  // Method to display
  def render() {
    imageMode(CENTER);
    tint(255, lifespan)
    image(img,loc.x,loc.y);
    // Drawing a circle instead
    // fill(255,lifespan);
    // noStroke();
    // ellipse(loc.x,loc.y,img.width,img.height);
  }

  // Is the particle still useful?
  def isDead() = {
    if (lifespan <= 0f) {
      true
    } else {
      false
    }
  }
}
