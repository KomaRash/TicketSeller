package TicketSeller

import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

/**
 * traits uses for get [[akka.util.Timeout]] from Config file
 */
trait ConfigLoadApi{
  def config: Config = ConfigFactory.load()
}
trait TicketSellerTimeout extends ConfigLoadApi {
  import scala.concurrent.duration._
  def timeout(path:String): Timeout = {
    val t = config.getString(path)
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
trait RequestTimeout extends TicketSellerTimeout {
  def requestTimeout: Timeout =timeout("akka.http.server.request-timeout")
}
trait AuthorizeTimeout extends TicketSellerTimeout{
  def userAskTimeout: Timeout =timeout("Users.userAskTime")
  def userRefreshTokenTimeout: Timeout = timeout("Users.refreshTokenTime")
}

