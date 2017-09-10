package utils

import javax.inject.Inject

class AllProperties @Inject()(configuration: play.api.Configuration) {
  if(configuration.get[String]("url.platform.stackoverflow") == null){
    throw new IllegalStateException("stackoverflow url not configured")
  }
  if(configuration.get[String]("url.platform.auth") == null){
    throw new IllegalStateException("auth url not configured")
  }

  val stackoverflowURL: String = configuration.get[String]("url.platform.stackoverflow")
  val authURL: String = configuration.get[String]("url.platform.auth")
}
