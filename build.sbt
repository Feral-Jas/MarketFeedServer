ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val AkkaVersion = "2.6.18"
val AkkaHttpVersion = "10.2.7"

val AkkaStack = Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

val CommonStack = Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.17.1",
  "io.spray" %% "spray-json" % "1.3.6",
  "ch.qos.logback" % "logback-classic" % "1.2.10"
)

lazy val root = (project in file("."))
  .settings(
    name := "MarketFeedServer",
    libraryDependencies ++=
      AkkaStack ++ CommonStack
  )
