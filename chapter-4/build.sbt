import sbt.Keys._

name := "chapter-4"

version := "1.0"

lazy val `chapter-4` = (project in file(".")).aggregate(`web-app`,
  `auth-app`,
  `so-app`,
  `github-app`,
  `rank-app`,
  `dev-mapping`
  , commons)

lazy val commonSettings: Seq[_root_.sbt.Def.Setting[_ >: Task[Seq[String]] with Seq[Resolver] with String <: Object]] = Seq(
  organization := "com.scalamicroservices",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  scalaVersion := "2.11.6",
  resolvers ++= Seq("Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "JBoss" at "https://repository.jboss.org/")
)

lazy val commons = BaseProject("commons")
  .settings(libraryDependencies ++= Seq(json, specs2 % Test))

lazy val `web-app` = PlayProject("web-app")
  .settings(libraryDependencies ++= Seq(parserCombinator, json, cache, ws, specs2 % Test))
  .dependsOn(commons)

lazy val `so-app` = PlayProject("so-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions))
  .dependsOn(commons)

lazy val `auth-app` = PlayProject("auth-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions))
  .dependsOn(commons)

lazy val `rank-app` = PlayProject("rank-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions))
  .dependsOn(commons)

lazy val `github-app` = PlayProject("github-app")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions))
  .dependsOn(commons)

lazy val `dev-mapping` = PlayProject("dev-mapping")
  .settings(libraryDependencies ++= Seq(h2, jbcrypt, slick, playSlick, playSlickEvolutions))
  .dependsOn(commons)


def BaseProject(name: String): Project = (
  Project(name, file(name))
    settings (commonSettings: _*)
  )

def PlayProject(name: String): Project = (
  BaseProject(name)
    enablePlugins PlayScala
  )


val playV = "2.5.10"
val slickV = "3.1.1"
val h2V = "1.4.193"
val playSlickV = "2.0.2"
val jbcryptV = "0.4"
val parserCombinatorV = "1.0.5"

val slick = "com.typesafe.slick" %% "slick" % slickV
val slickHikariCP = "com.typesafe.slick" %% "slick-hikaricp" % slickV
val h2 = "com.h2database" % "h2" % h2V
val playSlick = "com.typesafe.play" %% "play-slick" % playSlickV
val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % playSlickV
val jbcrypt = "org.mindrot" % "jbcrypt" % jbcryptV
val parserCombinator = "org.scala-lang.modules" % "scala-parser-combinators_2.11" % parserCombinatorV

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