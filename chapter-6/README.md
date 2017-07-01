A Lagom Scala template for Lightbend Activator showcasing a twitter-like application. 
This is built on top of the exising [lagom-scala-chirper](https://github.com/dotta/activator-lagom-scala-chirper).

There are a few changes:
* It uses the scaladsl api instead of javadsl from scala
* For the sake of learning. It is bare minimum and does not use the Persistence API. All the data is simply stored on-memory using Map 

Start all services using `mvn lagom:runAll` or `sbt runAll`.
