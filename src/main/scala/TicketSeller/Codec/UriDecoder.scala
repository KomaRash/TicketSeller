package TicketSeller.Codec



trait UriDecoder extends DateTimeCodec{
  import TicketSeller.EventOperations.EventOperations.TicketSellerDateTime
  import org.joda.time.LocalDateTime
  import TicketSeller.EventOperations.Event

  /**
   * def convert string segment of http request to Event object without place and event information
   * @param uriPath - string segment of http
   * @return [[TicketSeller.EventOperations.Event]]
   */
  def fromUriToEvent(uriPath:String):Either[String,Event]=uriPath.split("--").toList match {
    case name::dateTime::Nil=>Right(Event(name=name,dateTime=TicketSellerDateTime(LocalDateTime.parse(dateTime,datetimeFormat))))
    case _=>Left("Event not found")
  }
}
