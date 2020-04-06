package TicketSeller.Codec

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

trait DateTimeCodec {
  /**
   * this codec use for work with data in request
   * from user/response to database for convert [[String]]  [[org.joda.time.DateTime]]
   */
  val datetimeFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy-HH-m");

}
