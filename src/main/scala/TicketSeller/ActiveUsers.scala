package TicketSeller

import TicketSeller.EventOperations.AccessLevel._
import TicketSeller.EventOperations.EventOperations.EventRequest
import akka.actor.{Actor, Props}

import scala.reflect.runtime.universe._

object ActiveUsers {
  def property: Props = Props(new ActiveUsers())

  def name="ActiveUsers"

}
class ActiveUsers() extends Actor{

  override def receive: Receive = {
    case eventRequest:EventRequest[]=>UserSort(eventRequest)
  }

}
