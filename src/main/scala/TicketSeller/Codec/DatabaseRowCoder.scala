package TicketSeller.Codec

import TicketSeller.EventOperations.AccessLevel.AL
import TicketSeller.EventOperations.EventOperations.{Event, EventInfo, TicketSellerDateTime}
import TicketSeller.EventOperations.User.UserInfo
import TicketSeller.EventOperations.{AccessLevel, Place, User}
import cats.implicits._
import org.joda.time.LocalDateTime
import scalikejdbc.WrappedResultSet
import scalikejdbc.jodatime.JodaTypeBinder._
trait DatabaseRowCoder {

  implicit class WrappedResultSetOpt( result: WrappedResultSet) {
    //Not realize table Ticket and ticket sell
    /*def toTicket: Ticket = {
      Ticket(result.string("EventName"),
        result.int("TicketId"),
        result.string("TicketType"))
    }
    */
    /**
     * def for extract info about Event from the database without
     * [[TicketSeller.EventOperations.Role]] and
     * [[TicketSeller.EventOperations.EventOperations.EventInfo]]
     * @return `()=> Event`
     */
    def toEventWithoutInfo: () => Event = ()=>
      Event(None,
        result.string("EventName"),
        None,
        None,
        TicketSellerDateTime(result.get[LocalDateTime]("DATETIME")))

    /**
     * extract info about PLace for Event from the database
     * @return Option  [[TicketSeller.EventOperations.Place]]
     */
    def toPlaceWithoutId: () => Option[Place] = ()=>Option(
      Place( name=result.string("Name"),
        address=result.string("Address")))
    def toEventWithPlace: () => Event = toEventWithoutInfo.map(_.copy(place = toPlaceWithoutId()))
    def toEventInfo: () => Option[EventInfo] = ()=>Option(EventInfo(result.stringOpt("Preview")))
    def toUserEventInfo: () => Event = toEventWithPlace.map(_.copy(eventInfo = toEventInfo ()))

    def toUserInfo=UserInfo(userMail=result.string("UserMail"))

    /**
    def toFullUserInfo=toUserInfo.copy(password=result.stringOpt("Password"))
     * extract info about UserRole from WrappedResultSet
     * @return userRole: [[TicketSeller.EventOperations.AccessLevel]]
     */
    def toUserRole:AL =AccessLevel.withName(result.string("Role"))

    /**
     * extract from WrappedResultSet about User without Password
     * @return user: [[TicketSeller.EventOperations.User]]
     */
    def toUser=User(result.string("UserNickName"),Option(result.toUserInfo),None,toUserRole)
  }


}
