package TicketSeller.Codec

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

trait DateTimeCodec {
  val datetimeFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy-HH-m");

}
