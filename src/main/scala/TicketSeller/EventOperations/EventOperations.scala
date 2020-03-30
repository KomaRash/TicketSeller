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


  sealed trait EventRequest
  case class GetEvent(event: Event, user:Role)extends EventRequest
  case class GetEvents(user:Role)extends EventRequest
//  case class AuthorizeUserRequest[AL<:AccessLevel.Authorized](userInfo: UserInfo)extends EventRequest[AL]

  sealed trait EventResponse

  case class CancelEventResponse(message:String ="", user:Role) extends EventResponse
  case class GetEventsResponse(eventList:List[Event],user:Role) extends EventResponse
  case class GetEventResponse(event: Event,user:Role) extends EventResponse
  case class AuthorizeUserResponse(user:User) extends EventResponse

  type Id=Int
  type TicketType=String

}
