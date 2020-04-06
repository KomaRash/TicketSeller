package TicketSeller


import TicketSeller.Codec.JsonCodec
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding

import scala.concurrent.Future

object Main extends App  with RequestTimeout
                         with JsonCodec {
  val host = config.getString("http.host") // Gets the host and a port from the configuration
  val port = config.getInt("http.port")
    println(port)
  implicit val system = ActorSystem()
  implicit val dispatcher = system.dispatcher
  val api=new RestApi(system,requestTimeout).routes
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api, host, port)
}

