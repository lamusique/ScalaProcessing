package com.nekopiano.scala.processing.sandbox.sound

import ddf.minim.Minim
import ddf.minim.ugens.{Oscil, Waves}

/**
  * Created on 13/08/2016.
  */
object MinimTest extends App {


  val minim = new Minim(this);

  // use the getLineOut method of the Minim object to get an AudioOutput object
  val out = minim.getLineOut();

  // create a sine wave Oscil, set to 440 Hz, at 0.5 amplitude
  val wave = new Oscil( 440, 0.5f, Waves.SINE );

  wave.patch( out );

}
