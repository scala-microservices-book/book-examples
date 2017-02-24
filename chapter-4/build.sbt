import sbt.Keys._

name := "chapter-4"

version := "1.0"

lazy val `chapter-4` = (project in file(".")).aggregate(`web-app`, `so-app`, commons)

lazy val commonSettings = Seq(
  organization := "com.scalamicroservices",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  scalaVersion := "2.11.6",
  resolvers ++= Seq("rediscala" at "http://dl.bintray.com/etaty/maven",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "JBoss" at "https://repository.jboss.org/")
)


lazy val `web-app` = PlayProject("web-app").settings(libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test))

lazy val `so-app` = PlayProject("so-app").settings(libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test))

lazy val commons = BaseProject("commons")



def BaseProject(name: String): Project = (
  Project(name, file(name))
    settings (commonSettings: _*)
  )

def PlayProject(name: String): Project = (
  BaseProject(name)
    enablePlugins PlayScala
  )


val playV = "2.5.10"
val slickV = "2.1.0"

val slick = "com.typesafe.slick" %% "slick" % slickV
val `play-ws` = "com.typesafe.play" %% "play-ws" % playV
val `play-json` = "com.typesafe.play" %% "play-json" % playV

val runAll = inputKey[Unit]("Runs all subprojects")

runAll := {
  (run in Compile in `web-app`).evaluated
  (run in Compile in `so-app`).evaluated
}

fork in run := true

// enables unlimited amount of resources to be used :-o just for runAll convenience
concurrentRestrictions in Global := Seq(
  Tags.customLimit(_ => true)
)
