package TicketSeller.EventOperations

sealed trait EventRequest
case class GetEvent(event: Event, user:Role)extends EventRequest
case class GetEvents(user:Role)extends EventRequest
