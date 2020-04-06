package TicketSeller.EventOperations.Info

import TicketSeller.EventOperations.EventOperations.Id


/**
 *  this case class constrains info about place carrying out Events
 *
 * @param id - Id place in database
 * @param name - Place names (club, concert hall, etc.)
 * @param address - Event Location
 */
case class Place(id:Option[Id]=None,
                 name:String,
                 address:String)
