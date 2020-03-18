package TicketSeller

import TicketSeller.EventOperations.EventOperations._
import akka.actor.{Actor, Props}
import scalikejdbc._ // for Functor import cats.syntax.functor.__
object Database extends DatabaseRowCoder {
  import Main.config
  def name: String = "ticketsDatabase"
  def property: Props = Props(new Database())
  private def jdbc: String = config.getString("Database.url")
  private def passwordDB: String = config.getString("Database.password")
  private def userDB: String = config.getString("Database.user")

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton(jdbc,
    userDB, passwordDB)
  implicit val session: DBSession = AutoSession

}
class Database() extends Actor{
  import Database._
  def getEvents= (sql""" select * from event""".map(_.toEventWithoutInfo()).list().apply())
  def getEvent(event: Event)= {
    sql""" select * from event natural join place where
        EventName=${event.name} and DATETIME = ${event.dateTime.dateTime} """.map(_.toUserEventInfo()).single().apply()


  }
  override def receive: Receive = {
  //  case CreateEvent(event,tickets,ticketType)=>sender()! addEvent(event,ticketType,tickets)
    case GetEvent(event,user,_)=>sender() ! {
      getEvent(event) match {
        case Some(value) => GetEventResponse(value, user)
        case None => CancelEventResponse("Event not Found",user)
      }
    }
    case GetEvents(user,_)=>sender() ! GetEventsResponse(getEvents,user)
  }
}
