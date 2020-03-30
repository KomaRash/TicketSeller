package TicketSeller

import TicketSeller.Codec.DatabaseRowCoder
import TicketSeller.EventOperations.EventOperations._
import TicketSeller.EventOperations.Unauthorized
import TicketSeller.EventOperations.User.UserInfo
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
  def getUser(userInfo: UserInfo) =sql"""SELECT * FROM users where UserMail=${userInfo.userMail} and Password=${userInfo.password}
       """.map(_.toUser).single().apply()
  override def receive: Receive = {
  //  case CreateEvent(event,tickets,ticketType)=>sender()! addEvent(event,ticketType,tickets)
    case GetEvent(event,user)=>sender() ! {
      getEvent(event) match {
        case Some(value) => GetEventResponse(value, user)
        case None => CancelEventResponse("Event not Found",user)
      }
    }
    case GetEvents(user)=>sender() ! GetEventsResponse(getEvents,user)
    case userInfo:UserInfo=>
      //println(userInfo)
      val a=getUser(userInfo).map(AuthorizeUserResponse).getOrElse(CancelEventResponse("NotUser",Unauthorized))

      sender() ! a
  }

}
