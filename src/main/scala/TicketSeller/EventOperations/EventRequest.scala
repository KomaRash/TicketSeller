package TicketSeller.EventOperations

import TicketSeller.EventOperations.User.Token

sealed trait EventRequest
case class GetEvent(event: Event, user:Role)extends EventRequest
case class GetEvents(user:Role)extends EventRequest
case class RefreshToken(accessToken:Token,refreshToken:Token)
