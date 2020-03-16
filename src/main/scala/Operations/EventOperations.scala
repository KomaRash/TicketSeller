package Operations

import Operations.AccessLevel.AccessLevel
import org.joda.time.LocalDateTime

object EventOperations {

  case class  Event(id:Option[Int]=None,
                    name:String,
                    eventInfo: Option[EventInfo]=None,
                    place:Option[Place]=None,
                    dateTime:EventDateTime )

  case class EventInfo(/*tickets:Map[TicketType,Int],*/preview:Option[String])
  case class EventDateTime(dateTime:LocalDateTime)
  case class Ticket(event:String,ticketId: Int,ticketType:TicketType)

  sealed trait Action[AL <: AccessLevel]
  sealed trait EventRequest[AL] extends Action[AL]

  /*case class CreateEvent[T](event: Event,tickets:Int,ticketType:TicketType) extends EventRequest[T]{
    require(tickets>0)
  }*/
  case class GetEvent[AL](event: Event,  user:Role[AL])extends EventRequest[AL]
  case class GetEvents[AL](user:Role[AL]) extends EventRequest[AL]
  //case class GetTickets[AL](name:String,tickets:Int,user:Role[AL]) extends EventRequest[AL]

  sealed trait EventResponse[AL] extends Action[AL]

  case class CancelEventResponse[AL](message:String ="", user:Role[AL]) extends EventResponse[AL]
  case class GetEventsResponse[AL](eventList:List[Event],user:Role[AL]) extends EventResponse[AL]
  case class GetEventResponse[AL](event: Event,user:Role[AL]) extends EventResponse[AL]


  type Id=Int
  type TicketType=String

}
