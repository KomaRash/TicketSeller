package TicketSeller.EventOperations

import org.joda.time.LocalDateTime

package object EventOperations {
  case class  Event(id:Option[Int]=None,
                    name:String,
                    eventInfo: Option[EventInfo]=None,
                    place:Option[Place]=None,
                    dateTime:EventDateTime )

  case class EventInfo(/*tickets:Map[TicketType,Int],*/preview:Option[String])
  case class EventDateTime(dateTime:LocalDateTime)
  case class Ticket(event:String,ticketId: Int,ticketType:TicketType)


  sealed trait EventRequest[AL<:AccessLevel.AccessLevel]
  case class GetEvent[AL<:AccessLevel.Anon](event: Event, user:Role[AL])extends EventRequest[AL]
  case class GetEvents[AL<:AccessLevel.Anon](user:Role[AL])extends EventRequest[AL]
  case class AuthorizeUserRequest[AL<:AccessLevel.Authorized](userInfo: UserInfo)extends EventRequest[AL]

  sealed trait EventResponse[+AL<:AccessLevel.AccessLevel]

  case class CancelEventResponse[AL<:AccessLevel.Anon](message:String ="", user:Role[AL]) extends EventResponse[AL]
  case class GetEventsResponse[AL<:AccessLevel.Anon](eventList:List[Event],user:Role[AL]) extends EventResponse[AL]
  case class GetEventResponse[AL<:AccessLevel.Authorized](event: Event,user:Role[AL]) extends EventResponse[AL]


  type Id=Int
  type TicketType=String

}
