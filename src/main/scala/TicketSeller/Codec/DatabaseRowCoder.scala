package TicketSeller.Codec

import TicketSeller.EventOperations.AccessLevel.AL
import TicketSeller.EventOperations.EventOperations.{Event, EventDateTime, EventInfo, Ticket}
import TicketSeller.EventOperations.User.UserInfo
import TicketSeller.EventOperations.{AccessLevel, Place, User}
import cats.implicits._
import org.joda.time.LocalDateTime
import scalikejdbc.WrappedResultSet
import scalikejdbc.jodatime.JodaTypeBinder._
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
    def toUserRole:AL =AccessLevel.withName(result.string("Role"))

    def toUser=User(result.string("UserNickName"),Option(result.toUserInfo),None,toUserRole)
  }


}
