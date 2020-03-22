package TicketSeller

import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}


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
  def requestTimeout: Timeout =super.timeout("akka.http.server.request-timeout")
}
trait AuthorizeTimeout extends TicketSellerTimeout{
  def userAskTimeout: Timeout =super.timeout("Users.userAskTime")
  def userRefreshTokenTimeout: Timeout = super.timeout("Users.refreshTokenTime")
}

