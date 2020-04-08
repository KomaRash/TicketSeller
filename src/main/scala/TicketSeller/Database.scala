package TicketSeller

import TicketSeller.Codec.DatabaseRowCoder
import TicketSeller.EventOperations._
import TicketSeller.EventOperations.User.UserInfo
import akka.actor.{Actor, Props}
import scalikejdbc._
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
  import EventOperations.equalsEventSemigroup
  import cats.implicits._
  def getEvents= sql""" select * from event""".
                              map(_.toEventWithoutInfo()).list().apply()
  def getEvent(event: Event)= {
    sql"""select * ,count(*) as Tickets from tickets as t left join
            (select * from event as ev natural join place as p where
                ev.EventName=${event.name} and
                ev.DATETIME = ${event.dateTime.dateTime})
            as events on (t.eventId=events.EventId )
            group by ticketType,t.userId
                having t.userId is null;""".
      map(_.toEventWithEventInfo()).list().apply().reduceOption{(x,y)=> x |+| y}


  }
  def getUser(userInfo: UserInfo) =sql"""SELECT * FROM users
                                       where UserMail=${userInfo.userMail} and
                                       Password=${userInfo.password}""".
                                        map(_.toUser).single().apply()
  override def receive: Receive = {

    case GetEvent(event,user)=>sender() ! {
      getEvent(event) match {
        case Some(value) => GetEventResponse(value, user)
        case None => CancelEventResponse("Event not Found",user)
      }
    }
    case GetEvents(user)=>sender() ! GetEventsResponse(getEvents,user)

    case userInfo:UserInfo=>
      val authorizeResponse=getUser(userInfo).map(AuthorizeUserResponse).getOrElse(CancelEventResponse("NotUser",Unauthorized))
      sender() ! authorizeResponse
  }

}
