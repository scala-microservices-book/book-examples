package service.impl

import java.util.concurrent.Executors

import play.api.libs.ws.{WS, WSResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object Prac extends App {
  println(Runtime.getRuntime.availableProcessors())

  import scala.concurrent.blocking

  def square(n:Int) = Future{
    math.sqrt(100)
  }

  Thread.sleep(50000)

}
