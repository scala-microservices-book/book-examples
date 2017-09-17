package utils

import javax.inject.Inject

class AllProperties @Inject()(configuration: play.api.Configuration) {
  if(configuration.get[String]("url.platform.rank") == null){
    throw new IllegalStateException("rank url not configured")
  }
  if(configuration.get[String]("url.platform.auth") == null){
    throw new IllegalStateException("auth url not configured")
  }

  val rankURL: String = configuration.get[String]("url.platform.rank")
  val authURL: String = configuration.get[String]("url.platform.auth")
}
