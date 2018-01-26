name := "goto-vac"

version := "0.1"

scalaVersion := "2.12.4"

lazy val `goto-vac` = (project in file(".")).aggregate(backend, frontend, modelJvm, modelJs)

lazy val backend =
  (project in file("backend"))
    .dependsOn(modelJvm)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-http" % "10.0.11"
      ),
      resourceGenerators in Compile += Def.task {
        val f1 = (fastOptJS in Compile in frontend).value
        val f2 = (packageScalaJSLauncher in Compile in frontend).value
        Seq(f1.data, f2.data)
      }.taskValue,
      watchSources ++= (watchSources in frontend).value
    )

lazy val frontend =
  (project in file("frontend"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "scalatags" % "0.6.7",
        "org.scala-js" %%% "scalajs-dom" % "0.9.2"
      )
    )
    .dependsOn(modelJs)

lazy val model = crossProject.crossType(CrossType.Pure) in file("model")

lazy val modelJs = model.js.settings(libraryDependencies ++= Seq("com.lihaoyi" %%% "upickle" % "0.5.1"))
lazy val modelJvm = model.jvm.settings(libraryDependencies ++= Seq("com.lihaoyi" %% "upickle" % "0.5.1"))