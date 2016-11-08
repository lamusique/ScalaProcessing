package com.nekopiano.scala.processing.sandbox.sound

import net.beadsproject.beads.core.AudioContext
import net.beadsproject.beads.data.Buffer
import net.beadsproject.beads.ugens.{Envelope, Gain, WavePlayer}

/**
  * Created on 13/08/2016.
  */
object BeadsTest extends App {

  val ac = new AudioContext();
  /*
   * This is an Envelope. It can be used to modify
   * the behaviour of other UGen object. We need to
   * do this to get precise control of certain parameters
   * at an audio rate.
   */
  val freqEnv = new Envelope(ac, 500);
  /*
   * This is a WavePlayer. Here we've set it up using
   * the above Envelope, and a SineBuffer. We'll use
   * the Envelope to modify the freqency below.
   */
  val wp = new WavePlayer(ac, freqEnv, Buffer.SINE);
  /*
   * So now that the WavePlayer is set up with the
   * frequency Envelope, do stuff with the frequency
   * envelope. This command tells the Envelope to change
   * to 1000 in 1 second. Note that when we made the Envelope
   * it was set to 500, so the transition goes from 500 to
   * 1000. These control the frequency of the WavePlayer
   * in Hz.
   */
  freqEnv.addSegment(1000, 1000);
  /*
   * Connect it all together as before.
   */
  val g = new Gain(ac, 1, 0.1f);
  g.addInput(wp);
  ac.out.addInput(g);
  ac.start();


}
