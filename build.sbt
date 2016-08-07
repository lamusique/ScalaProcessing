name := "ScalaProcessing"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.8"


// http://mvnrepository.com/artifact/org.processing
//libraryDependencies += "org.processing" % "processing-complete" % "3.0"

// Checking the POM file
// https://repo1.maven.org/maven2/org/processing/core/3.1/core-3.1.pom
libraryDependencies += "org.processing" % "core" % "3.1.1" withSources()
libraryDependencies += "org.jogamp.jogl" % "jogl-all" % "2.3.2"
libraryDependencies += "org.jogamp.jogl" % "jogl-all-main" % "2.3.2"
libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2"
libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt-main" % "2.3.2"

libraryDependencies += "org.processing" % "pdf" % "3.1.1" withSources()
libraryDependencies += "com.lowagie" % "itext" % "4.2.1" withSources()

//libraryDependencies += "org.processing" % "svg" % "3.1.1" withSources()

//libraryDependencies += "org.processing" % "processing-complete" % "3.1.1" withSources()


// Read here for optional jars and dependencies
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.8.4" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")
