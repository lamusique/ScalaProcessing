name := "scala-processing-root"

lazy val commonSettings = Seq(
  organization := "com.nekopiano.scala",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    // other settings
  ).
  aggregate(core, experiments, samples, sandbox)

lazy val core = (project in file("core")).
  settings(commonSettings: _*).
  settings(
    // other settings
  )

lazy val experiments = (project in file("experiments")).
  settings(commonSettings: _*).
  settings(
    // other settings
  ).
  dependsOn(core)

lazy val samples = (project in file("samples")).
  settings(commonSettings: _*).
  settings(
    // other settings
  ).
  dependsOn(core, experiments)

lazy val sandbox = (project in file("sandbox")).
  settings(commonSettings: _*).
  settings(
    // other settings
  ).
  dependsOn(core, experiments)
