package TicketSeller.EventOperations

import TicketSeller.EventOperations.EventOperations._
import TicketSeller.EventOperations.Info.Place
import org.joda.time.LocalDateTime

/**
 *
 */
package object EventOperations {
  case class EventInfo(/*tickets:Map[TicketType,Int],*/preview:Option[String])
  case class TicketSellerDateTime(dateTime:LocalDateTime){
    def apply():LocalDateTime=dateTime
  }

  case class Ticket(event:String,ticketId: Int,ticketType:TicketType)

  type Id=Int

  type TicketType=String

}
case class  Event(id:Option[Id]=None,
                  name:String,
                  eventInfo: Option[EventInfo]=None,
                  place:Option[Place]=None,
                  dateTime:TicketSellerDateTime )


