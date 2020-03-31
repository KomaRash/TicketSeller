package TicketSeller.Codec

import TicketSeller.EventOperations.EventOperations.TicketSellerDateTime

trait UriDecoder extends DateTimeCodec{
  import TicketSeller.EventOperations.EventOperations.Event
  import org.joda.time.LocalDateTime

  def fromUriToEvent(uriPath:String):Either[String,Event]=uriPath.split("--").toList match {
    case name::dateTime::Nil=>Right(Event(name=name,dateTime=TicketSellerDateTime(LocalDateTime.parse(dateTime,datetimeFormat))))
    case _=>Left("Event not found")
  }
}
