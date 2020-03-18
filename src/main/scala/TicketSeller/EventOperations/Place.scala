package TicketSeller.EventOperations

import TicketSeller.EventOperations.EventOperations.Id

case class Place(id:Option[Id]=None,
                 name:String,
                 address:String)

