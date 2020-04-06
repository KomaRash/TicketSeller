package TicketSeller.EventOperations


sealed trait EventResponse
case class CancelEventResponse(message:String ="", user:Role) extends EventResponse
case class GetEventsResponse(eventList:List[Event],user:Role) extends EventResponse
case class GetEventResponse(event: Event,user:Role) extends EventResponse
case class AuthorizeUserResponse(user:User) extends EventResponse

