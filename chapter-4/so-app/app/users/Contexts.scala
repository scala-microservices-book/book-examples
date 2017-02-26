package users


import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.Application

@Singleton
class Contexts @Inject()(akkaSystem: ActorSystem, configuration: play.api.Configuration) {
  implicit val dbLookup = akkaSystem.dispatchers.lookup("contexts.db-lookups")
  implicit val cpuLookup = akkaSystem.dispatchers.lookup("contexts.cpu-operations")
}