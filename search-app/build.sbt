name := "SearchApp"

version := "1.0"

lazy val `searchapp` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , ws  , guice , specs2 % Test )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
