name := "scala-processing-sample"

version := "1.0"

scalaVersion := "2.11.8"


lazy val core = (project in file("../core"))

lazy val samples = (project in file(".")).
  dependsOn(core)


libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.8"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.10"


// Sound
libraryDependencies += "de.sciss" %% "scalacollider" % "1.20.1"


// CSV
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.1.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.12.0"
libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.2.0",
  "org.joda" % "joda-convert" % "1.7"
)

// Logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "com.typesafe.play" %% "play-logback" % "2.5.4"
libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.2" // Scala-JVM


// Testing
// Read here for optional jars and dependencies
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.8.4" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

