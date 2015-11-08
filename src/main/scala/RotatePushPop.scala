import processing.core.PApplet
import processing.core.PConstants

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

object RotatePushPop extends PApplet {

/**
 * Rotate Push Pop. 
 * 
 * The push() and pop() functions allow for more control over transformations.
 * The push function saves the current coordinate system to the stack 
 * and pop() restores the prior coordinate system. 
 */
 
var a:Float = 0.0f;               // Angle of rotation
var offset:Float = PConstants.PI/24.0f;  // Angle offset between boxes
var num:Int = 12;            // Number of boxes

override def setup() {
  noStroke()
}

override def draw() {

  lights();
  
  background(0, 0, 26);
  translate(width/2, height/2); 
  
  for (i <- 0 to num) {
    val gray = PApplet.map(i, 0, num-1, 0, 255);
    pushMatrix();
    fill(gray);
    rotateY(a + offset*i);
    rotateX(a/2 + offset*i);
    box(200);
    popMatrix();
  }
  
  a += 0.01f;    
} 

  //override def settings() {  size(640, 360, PConstants.P3D); }
  override def settings() {  size(640, 360, PConstants.P3D); }

  def main(args: Array[String]) {

    val libPath = System.getProperty("java.library.path")
    println(libPath)
    val userDir = System.getProperty("user.home")
    println(userDir)
    System.setProperty("java.library.path", libPath +";"+"\\.ivy2\\cache\\org.jogamp.gluegen\\gluegen-rt\\jars")
    //"C:\\Users\\neko\\.ivy2\\cache\\org.jogamp.gluegen\\gluegen-rt\\jars\\gluegen-rt-2.3.2-natives-windows-i586.jar"

    val appletArgs = Array("JavaRotatePushPop")
    if (args != null) {
      PApplet.main(PApplet.concat(appletArgs, args));
    } else {
      PApplet.main(appletArgs);
    }
  }

}

