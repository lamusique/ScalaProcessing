import processing.core.{PConstants, PApplet}

/**
 * Esfera
 * by David Pena.  
 *
 * Distribucion aleatoria uniforme sobre la superficie de una esfera. 
 */

class Esfera extends PApplet {

  val cuantos = 16000
  val lista = (0 to cuantos).map { i => {
    new Pelo()
  }}

  val radio = 200f;
  var rx = 0f;
  var ry = 0f;

  override def setup() {
    val radio = height / 3.5
    noiseDetail(3)
  }

  override def settings() {
    size(1024, 768, PConstants.P3D)
  }

  override def draw() {
    background(0)

    val rxp = (mouseX - (width / 2)) * 0.005f
    val ryp = (mouseY - (height / 2)) * 0.005f
    rx = rx * 0.9f + rxp * 0.1f
    ry = ry * 0.9f + ryp * 0.1f

    translate(width / 2, height / 2)
    rotateY(rx)
    rotateX(ry)
    fill(0)
    noStroke()
    sphere(radio)

    lista.foreach(pelo => {
      pelo.dibujar()
    })

  }

  class Pelo {
    var z = random(-radio, radio);
    var phi = random(PConstants.TWO_PI);
    var largo = random(1.15f, 1.2f);
    var theta = PApplet.asin(z / radio);

    //this() = {
    {
      // what's wrong with a constructor here
      z = random(-radio, radio);
      phi = random(PConstants.TWO_PI);
      largo = random(1.15f, 1.2f);
      theta = PApplet.asin(z / radio);
    }

    //}

    def dibujar() {

      val off = (noise(millis() * 0.0005f, PApplet.sin(phi)) - 0.5f) * 0.3f;
      val offb = (noise(millis() * 0.0007f, PApplet.sin(this.z) * 0.01f) - 0.5f) * 0.3f;

      val thetaff = theta + off;
      val phff = phi + offb;
      val x = radio * PApplet.cos(theta) * PApplet.cos(phi);
      val y = radio * PApplet.cos(theta) * PApplet.sin(phi);
      val z = radio * PApplet.sin(theta);

      val xo = radio * PApplet.cos(thetaff) * PApplet.cos(phff);
      val yo = radio * PApplet.cos(thetaff) * PApplet.sin(phff);
      val zo = radio * PApplet.sin(thetaff);

      val xb = xo * largo;
      val yb = yo * largo;
      val zb = zo * largo;

      strokeWeight(1);
      beginShape(PConstants.LINES);
      stroke(0);
      vertex(x, y, z);
      stroke(200, 150);
      vertex(xb, yb, zb);
      endShape();
    }
  }

}

object Esfera {
  def main(args: Array[String]) {

    // This specifies the class to be instantiated.
    val appletArgs = Array("Esfera")
    if (args != null) {
      PApplet.main(PApplet.concat(appletArgs, args))
    } else {
      PApplet.main(appletArgs)
    }
  }

}
