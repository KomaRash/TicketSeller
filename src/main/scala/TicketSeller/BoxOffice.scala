package TicketSeller

import TicketSeller.EventOperations.EventOperations._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.EventStream
import akka.util.Timeout

import scala.concurrent.ExecutionContext
object BoxOffice {
def name="TicketSeller.BoxOffice"
def property(implicit system:ActorSystem,timeout: Timeout)=Props(new BoxOffice())
  implicit val ec = ExecutionContext.global
  /*
  implicit val eventSemigroup: Semigroup[Event] = (x: Event, y: Event) =>
   Event(x.name, x.eventId, x.eventInfo |+| y.eventInfo)*/
}
class BoxOffice(implicit timeout: Timeout,system:ActorSystem ) extends Actor {
 implicit lazy val database: ActorRef =context.actorOf(Database.property,Database.name)
 val activeUsers:ActorRef=context.actorOf(ActiveUsers.property,ActiveUsers.name)
  lazy val eventStream: EventStream =system.eventStream
  //eventStream.subscribe(activeUsers,classOf[EventRequest[Anon]])
  import BoxOffice._
  import akka.pattern.{ask, pipe}
 override def receive: Receive = {

  case getEvents @(_: GetEvents[_] | _:GetEvent[_])=>
    database.ask(getEvents) pipeTo sender()
     //eventStream.publish(getEvents)
  /*case authorizeUser:AuthorizeUserRequest[_]=>activeUsers.ask(authorizeUser)*/
  case eventInfo: EventInfo=>activeUsers.ask(eventInfo)
 }
}


