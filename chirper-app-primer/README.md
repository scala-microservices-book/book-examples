A twitter like application built using Lightbend Lagom. 
This is built on top of the exising [lagom-scala-chirper](https://github.com/dotta/activator-lagom-scala-chirper) by the awesome [Mirco Dotta](https://twitter.com/mircodotta). It contains a few changes when compared to original repo:
* It uses the scaladsl api instead of javadsl from scala
* For the sake of learning. The code is bare minimum and only uses Lagom service api related concepts. Which means it:
  * It does not use the Persistence API. All the data is simply stored on-memory using Map
  * It neither uses the Message Broker API

Like it says: bare minimum

## Run

Start all services using `sbt runAll`. To access the application post the startup.: [http://localhost:9000](http://localhost:9000)
* You can then signup 
* Add a friend
* And chirp
