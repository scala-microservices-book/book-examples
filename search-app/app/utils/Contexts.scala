package utils

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.Akka

@Singleton
class Contexts @Inject() (akkaSystem: ActorSystem) {
  implicit val dbLookup = akkaSystem.dispatchers.lookup("contexts.db-lookups")
  implicit val expensiveDBLookup = akkaSystem.dispatchers.lookup("contexts.expensive-db-lookups")
  implicit val cpuLookup = akkaSystem.dispatchers.lookup("contexts.cpu-operations")
}
