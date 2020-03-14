
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Future

object Main extends App  with RequestTimeout with JsonCodec {
  val config = ConfigFactory.load()
  val host = config.getString("http.host") // Gets the host and a port from the configuration
  val port = config.getInt("http.port")
  implicit val system = ActorSystem()
  implicit val ее = system.dispatcher
  val api=new RestApi(system,requestTimeout(config)).routes
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api, host, port)
}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}