package TicketSeller.Operations

import TicketSeller.Operations.EventOperations.Id

case class Place(id:Option[Id]=None,
                 name:String,
                 address:String)
