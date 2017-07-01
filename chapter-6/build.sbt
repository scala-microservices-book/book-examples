organization in ThisBuild := "sample.chirper"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

lazy val friendApi = project("friend-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomScaladslApi
  )

lazy val friendImpl = project("friend-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(friendApi)

lazy val chirpApi = project("chirp-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val chirpImpl = project("chirp-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslPubSub,
      lagomScaladslTestKit,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(chirpApi)

lazy val activityStreamApi = project("activity-stream-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomScaladslApi
  )
  .dependsOn(chirpApi)

lazy val activityStreamImpl = project("activity-stream-impl")
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire
    )
  )
  .dependsOn(activityStreamApi, chirpApi, friendApi)

lazy val frontEnd = project("front-end")
  .enablePlugins(PlayScala, LagomPlay)
  .settings(
    version := "1.0-SNAPSHOT",
    routesGenerator := StaticRoutesGenerator,
    libraryDependencies ++= Seq(
      "org.webjars" % "react" % "0.14.3",
      "org.webjars" % "react-router" % "1.0.3",
      "org.webjars" % "jquery" % "2.2.0",
      "org.webjars" % "foundation" % "5.3.0",
      macwire,
      lagomScaladslServer
    ),
    ReactJsKeys.sourceMapInline := true
  )

//lazy val loadTestApi = project("load-test-api")
//  .settings(
//    version := "1.0-SNAPSHOT",
//    libraryDependencies += lagomScaladslApi
//  )
//
//lazy val loadTestImpl = project("load-test-impl")
//  .enablePlugins(LagomScala)
//  .settings(
//    version := "1.0-SNAPSHOT",
//    libraryDependencies ++= Seq(
//      macwire
//    )
//  )
//  .dependsOn(loadTestApi, friendApi, activityStreamApi, chirpApi, utils)

//lazy val utils = project("utils")
//  .settings(
//    version := "1.0-SNAPSHOT",
//    libraryDependencies += lagomScaladslApi
//  )

def project(id: String) = Project(id, base = file(id))
  .settings(
    scalacOptions in Compile += "-Xexperimental" // this enables Scala lambdas to be passed as Scala SAMs  
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.3" // actually, only api projects need this
    )
  )

licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

lagomCassandraEnabled in ThisBuild := false
