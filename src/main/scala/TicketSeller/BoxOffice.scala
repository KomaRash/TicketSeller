package TicketSeller

import TicketSeller.EventOperations.User.{Token, UserInfo}
import TicketSeller.EventOperations._
import akka.actor.{Actor, ActorRef, Props}
import akka.event.EventStream
import akka.util.Timeout

import scala.concurrent.ExecutionContext
object BoxOffice {
def name="TicketSeller.BoxOffice"
def property(implicit timeout: Timeout)=Props(new BoxOffice())
  implicit val ec = ExecutionContext.global
}
class BoxOffice(implicit timeout: Timeout) extends Actor {
 import BoxOffice._
 import akka.pattern.{ask, pipe}
 lazy implicit val database: ActorRef =context.actorOf(Database.property,Database.name)
 lazy val activeUsers:ActorRef=context.actorOf(ActiveUsers.property,ActiveUsers.name)
 lazy val eventStream: EventStream =context.system.eventStream
 //eventStream.subscribe(activeUsers,classOf[EventRequest[Anon]])
 override def receive: Receive = {

  case getEvents @(_: GetEvents | _:GetEvent)=>
    database.ask(getEvents) pipeTo sender()
     //eventStream.publish(getEvents)
  /*case authorizeUser:AuthorizeUserRequest[_]=>activeUsers.ask(authorizeUser)*/
  case info: UserInfo=>activeUsers.ask(info) pipeTo sender()
  case token:Token=>activeUsers.ask(token) pipeTo sender()
 }
}


