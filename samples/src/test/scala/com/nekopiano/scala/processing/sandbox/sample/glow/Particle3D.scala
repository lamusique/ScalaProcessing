package com.nekopiano.scala.processing.sandbox.sample.glow

import com.nekopiano.scala.processing.{ScalaPApp, ScalaPAppCompanion, ThreeDimensionalPApp}
import peasy.PeasyCam
import processing.core.PImage

/**
  * Created on 29/07/2016.
  */
class Particle3D extends ThreeDimensionalPApp {
// http://qiita.com/clomie/items/7134a4b627426a8269b4

  implicit val sp5 = this

  override def settings() {
    size(960, 540, P3D);
  }

  var cam: PeasyCam = null
  val particles = new Array[Particle](1000)

  override def setup() {
    hint(DISABLE_DEPTH_TEST);
    blendMode(SCREEN);
    imageMode(CENTER);
    frameRate(30);

    cam = new PeasyCam(this, width)
    cam.setMaximumDistance(width * 2)

    val images = Colors.values map (createLight(_))

    (0 until particles.length) foreach (i => {
      val image = images(i % images.size)
      particles(i) = Particle(image)
    })

  }


  def createLight(colors: Colors): PImage = {
    val side = 150
    val center = side / 2.0f
    val img = createImage(side, side, RGB)

    (0 until side) foreach (y => {
      (0 until side) foreach (x => {
        val distance = (sq(center - x) + sq(center - y)) / 10f
        val c = colors.calculate(distance)
        img.pixels(x + y * side) = c
      })
    })

    img;
  }

  var record = false

  override def draw() {

    background(0);
    translate(width / 2, height / 2, 0);

    import com.nekopiano.scala.processing.Angles._

    cam.rotateX(radians(0.25f));
    cam.rotateY(radians(0.25f));

    val rotations = cam.getRotations()
    particles foreach (p => {
      p.render(rotations)
    })

    if (record) {
      saveFrame("frame/frame-######.tif");
    }
  }

  override def keyPressed() {
    if (key == 's') {
      record = true;
    }

  }
}

object Particle3D extends ScalaPAppCompanion {}

case class Particle(light: PImage, x: Float, y: Float, z: Float)(implicit sp5: ScalaPApp) {

  import sp5._

  def render(rotation: Array[Float]) {
    pushMatrix();
    translate(x, y, z);
    rotateX(rotation(0));
    rotateY(rotation(1));
    rotateZ(rotation(2));
    image(light, 0, 0);
    popMatrix();
  }
}

object Particle {
  def apply(light: PImage)(implicit sp5: ScalaPApp) = {
    import sp5._
    import com.nekopiano.scala.processing.Angles._

    val radP = radians(random(360))

    val unitZ = random(-1, 1)
    val sinT = sqrt(1 - sq(unitZ))

    val unitR = pow(random(1), 1.0f / 3.0f)
    val r = width

    val x = r * unitR * sinT * cos(radP)
    val y = r * unitR * sinT * sin(radP)
    val z = r * unitR * unitZ
    new Particle(light, x, y, z)
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

  val values = Array(RED, ORANGE, YELLOW, LEAF, GREEN, EMERALD, CYAN, SKY, BLUE, PURPLE, MAGENTA)

  val SUPPRESS = 3f

  def color(a: Float, distance: Float): Int = {
    val color = (256 * a / distance - SUPPRESS).toInt
    return Math.max(0, Math.min(color, 255))
  }
}

sealed abstract class Colors(r: Float, g: Float, b: Float) {

  def calculate(d: Float): Int = 0xff << 24 | Colors.color(r, d) << 16 | Colors.color(g, d) << 8 | Colors.color(b, d)

}




