package TicketSeller

import TicketSeller.Operations.EventOperations._
import akka.actor.{Actor, Props}
import scalikejdbc._ // for Functor import cats.syntax.functor.__
object Database extends DatabaseRowCoder {
  import Main.config
  def name: String = "ticketsDatabase"
  def property: Props = Props(new Database())
  val jdbc: String = config.getString("Database.url")
  val passwordDB: String = config.getString("Database.password")
  val userDB: String = config.getString("Database.user")

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton(jdbc,
    userDB, passwordDB)
  implicit val session: DBSession = AutoSession

}
class Database() extends Actor{
  import Database._
  def getEvents= (sql""" select * from event""".map(_.toEventWithoutInfo()).list().apply())
  def getEvent(event: Event)= {
    println(event)
    sql""" select * from event natural join place where
        EventName=${event.name} and DATETIME = ${event.dateTime.dateTime} """.map(_.toUserEventInfo()).single().apply()


  }
  override def receive: Receive = {
  //  case CreateEvent(event,tickets,ticketType)=>sender()! addEvent(event,ticketType,tickets)
    case GetEvent(event,user)=>sender() ! {
      getEvent(event) match {
        case Some(value) => GetEventResponse(value, user)
        case None => CancelEventResponse("Event not Found",user)
      }
    }
    case GetEvents(user)=>sender() ! GetEventsResponse(getEvents,user)
  }
}
