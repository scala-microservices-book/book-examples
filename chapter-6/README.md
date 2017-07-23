A twitter like application built using Lightbend Lagom. 
This is built on top of the exising [lagom-scala-chirper](https://github.com/dotta/activator-lagom-scala-chirper) by [Mirco Dotta](https://twitter.com/mircodotta). It contains a few changes when compared to original repo:
* It uses the scaladsl api instead of javadsl from scala

## Chapter examples

### chapter-6
Chapter-6 example code can be found on the `master` branch of this repository.

* For the sake of learning. Chapter-6 code is bare minimum and does not use the Persistence API. All the data is simply stored on-memory using Map 

### chapter-7
Chapter-7 example code can be found on the `chapter-7` branch of this repository.

* Chapter-7 is built on top of the `chapter-6` code where it uses Lagom's Message Broker API to publish and subscribe


### chapter-8
Chapter-8 example code can be found on the `chapter-8` branch of this repository.

* Chapter-8 build on top of the `chapter-7` code where it uses Lagom's Persistence API

## Run

Start all services using `sbt runAll`. To access the application post the startup.: [http://localhost:9000](http://localhost:9000)
* You can then signup 
* Add a friend
* And chirp
