ThisBuild / scalaVersion := "3.5.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / javacOptions ++= Seq("-source", "23", "-target", "23", "--enable-preview")
ThisBuild / javaOptions ++= Seq("--enable-preview")

lazy val days2024 = (project in file("days"))
  .settings(
    name := "days2024",
    libraryDependencies ++=
      Seq("org.scalameta" %% "munit" % "1.0.2" % Test,
        "com.github.sbt.junit" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test
      )
  )

lazy val meta = (project in file("meta"))
  .settings(
    name := "meta2024",
    libraryDependencies += "com.lihaoyi" %% "requests" % "0.9.0",
  )