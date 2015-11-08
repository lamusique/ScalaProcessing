import processing.core.PApplet
import processing.core.PConstants

class RotatePushPop extends PApplet {

  /**
   * Rotate Push Pop.
   *
   * The push() and pop() functions allow for more control over transformations.
   * The push function saves the current coordinate system to the stack
   * and pop() restores the prior coordinate system.
   */

  // Angle of rotation
  var angle: Float = 0.0f

  // Angle offset between boxes
  // As this denominator increases, slices are finer.
  var offset: Float = PConstants.PI / 96f

  // Number of boxes
  val BOXES: Int = 120


  override def setup() {
    noStroke()
  }

  override def draw() {

    lights()
    background(0, 0, 26)
    translate(width / 2, height / 2)

    val seq = 0 to BOXES

    // Paint process needs to be serial.
    seq.foreach(i => {
      val gray = PApplet.map(i, 0, BOXES - 1, 0, 255)
      pushMatrix()
      fill(gray)
      rotateY(angle + offset * i)
      rotateX(angle / 2 + offset * i)
      box(200)
      popMatrix()
    })

    angle += 0.01f
  }

  override def settings() {
    size(640, 360, PConstants.P3D)
  }
}

object RotatePushPop {
  def main(args: Array[String]) {

    // This specifies the class to be instantiated.
    val appletArgs = Array("RotatePushPop")
    if (args != null) {
      PApplet.main(PApplet.concat(appletArgs, args))
    } else {
      PApplet.main(appletArgs)
    }
  }

}

