A twitter like application built using Lightbend Lagom. 
This is built on top of the exising [lagom-scala-chirper](https://github.com/dotta/activator-lagom-scala-chirper) by [Mirco Dotta](https://twitter.com/mircodotta). It contains a few changes when compared to original repo:
* It uses the scaladsl api instead of javadsl from scala

## Run

Start all services using `sbt runAll`. To access the application post the startup.: [http://localhost:9000](http://localhost:9000)
* You can then signup 
* Add a friend
* And chirp

Note: You may wish to run as `sbt -mem 2G` if you find it going Out of Memory Error
