package com.nekopiano.scala.sandbox.spec.logging

import com.typesafe.scalalogging.{LazyLogging, StrictLogging}

/**
  * Created on 14/08/2016.
  */
object ScalaLoggingApp extends App with LazyLogging {
//object ScalaLoggingApp extends App with StrictLogging {

  println("before logger")

  logger.trace("trace : " + "line=" + sourcecode.Line.generate + ", file=" + sourcecode.File.generate)
  logger.debug("debug : " + "line=" + sourcecode.Line.generate + ", file=" + sourcecode.File.generate)
  logger.info("info : " + "line=" + sourcecode.Line.generate + ", file=" + sourcecode.File.generate)
  logger.warn("warn : " + "line=" + sourcecode.Line.generate + ", file=" + sourcecode.File.generate)
  logger.error("error : " + "line=" + sourcecode.Line.generate + ", file=" + sourcecode.File.generate)

  testSourceCodesLibrary()

  println("after logger")

  def testSourceCodesLibrary()(implicit line: sourcecode.Line, file: sourcecode.File) = {
    logger.info("line=" + line + ", file=" + file)
  }
}
