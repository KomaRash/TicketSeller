package TicketSeller

import TicketSeller.Operations.EventOperations.{GetEvent, GetEvents}
import akka.actor.{Actor, ActorRef, Props}

import akka.util.Timeout

import scala.concurrent.ExecutionContext
object BoxOffice {
def name="TicketSeller.BoxOffice"
def property(implicit timeout: Timeout)=Props(new BoxOffice())
  implicit val ec = ExecutionContext.global
  /*
  implicit val eventSemigroup: Semigroup[Event] = (x: Event, y: Event) =>
   Event(x.name, x.eventId, x.eventInfo |+| y.eventInfo)*/
}
class BoxOffice(implicit timeout: Timeout) extends Actor {
 lazy val database: ActorRef =context.actorOf(Database.property,Database.name)
 import BoxOffice._
 import akka.pattern.{ask, pipe}
 override def receive: Receive = {

  case getEvents: GetEvents[_]=> database.ask(getEvents) pipeTo sender()
  case getEvent: GetEvent[_]=>database.ask(getEvent) pipeTo sender()
  }

}


