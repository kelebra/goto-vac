name := "goto-vac"

version := "0.1"

scalaVersion := "2.12.4"

lazy val `goto-vac` = (project in file(".")).aggregate(backend, frontend, modelJvm, modelJs)

lazy val backend = project in file("backend")

lazy val frontend = project in file("frontend")

lazy val model = crossProject.crossType(CrossType.Pure) in file("model")

lazy val modelJs = model.js
lazy val modelJvm = model.jvm