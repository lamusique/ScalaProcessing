name := "ScalaProcessing"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.7"

// http://mvnrepository.com/artifact/org.processing
//libraryDependencies += "org.processing" % "processing-complete" % "3.0"

// Checking the POM file
// https://repo1.maven.org/maven2/org/processing/core/3.0/core-3.0.pom
libraryDependencies += "org.processing" % "core" % "3.0" withSources()
libraryDependencies += "org.jogamp.jogl" % "jogl-all" % "2.3.2"
libraryDependencies += "org.jogamp.jogl" % "jogl-all-main" % "2.3.2"
libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2"
libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt-main" % "2.3.2"

