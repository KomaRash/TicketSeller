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


  sealed trait EventRequest

  case class CreateEvent(event: Event,tickets:Int,ticketType:TicketType) extends EventRequest{
    require(tickets>0)
  }
  case class GetEvent(event: Event)extends EventRequest
  case object GetEvents extends EventRequest
  case class GetTickets(name:String,tickets:Int) extends EventRequest

  sealed trait EventResponse


  case class CreateEventResponse(event: Event) extends EventResponse
  case class CancelEventResponse(message:String = "fail") extends EventResponse
  case class GetEventsResponse(eventList:List[Event]) extends EventResponse
  case class GetEventResponse(event: Event) extends EventResponse


  type Id=Int
  type TicketType=String

}
