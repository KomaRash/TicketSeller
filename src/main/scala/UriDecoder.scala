import EventOperations.EventDateTime

trait UriDecoder extends DateTimeCodec{
  import EventOperations.Event
  import org.joda.time.LocalDateTime

  def fromUriToEvent(uriPath:String):Either[String,Event]=uriPath.split("--").toList match {
    case name::dateTime::Nil=>Right(Event(name=name,dateTime=EventDateTime(LocalDateTime.parse(dateTime,datetimeFormat))))
    case _=>Left("Event not found")
  }
}
