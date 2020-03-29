package TicketSeller.Codec

import TicketSeller.EventOperations.AccessLevel.{Admin, Authorized, Redactor}
import TicketSeller.EventOperations.EventOperations.{Event, EventDateTime, EventInfo, Ticket}
import TicketSeller.EventOperations.{Place, User, UserInfo}
import cats.implicits._
import org.joda.time.LocalDateTime
import scalikejdbc.WrappedResultSet
import scalikejdbc.jodatime.JodaTypeBinder._

import scala.reflect.runtime.universe._
trait DatabaseRowCoder {

  implicit class WrappedResultSetOpt( result: WrappedResultSet) {
    def toTicket: Ticket = {
      Ticket(result.string("EventName"),
        result.int("TicketId"),
        result.string("TicketType"))
    }

    def toEventWithoutInfo: () => Event = ()=>
      Event(None,
        result.string("EventName"),
        None,
        None,
        EventDateTime(result.get[LocalDateTime]("DATETIME")))
    def toPlaceWithoutId: () => Option[Place] = ()=>Option(
      Place( name=result.string("Name"),
        address=result.string("Address")))
    def toUserInfo=UserInfo(userMail=result.string("UserMail"))
    def toFullUserInfo=toUserInfo.copy(password=result.stringOpt("Password"))
    def toEventWithPlace: () => Event = toEventWithoutInfo.map(_.copy(place = toPlaceWithoutId()))
    def toEventInfo: () => Option[EventInfo] = ()=>Option(EventInfo(result.stringOpt("Preview")))
    def toUserEventInfo: () => Event = toEventWithPlace.map(_.copy(eventInfo = toEventInfo ()))
    def toUserRole:TypeTag[_<: Authorized] =result.string("Role") match{
      case "Admin"=>typeTag[Admin]
      case "Redactor"=>typeTag[Redactor]
      case _=>typeTag[Authorized]
    }
    def toUser=User(result.string("UserNickName"),Option(result.toUserInfo),None,toUserRole)
  }


}
