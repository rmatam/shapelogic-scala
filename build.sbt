name := "shapelogic"

organization := "org.shapelogic.sc"

version := "0.0.2"

scalaVersion := "2.11.8"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "com.github.mpilquist" %% "simulacrum" % "0.10.0",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test" withSources() withJavadoc(),
  "org.spire-math" %% "spire" % "0.11.0"
)

initialCommands := "import org.shapelogic.sc.image._"

