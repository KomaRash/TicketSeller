import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
//LocalDateTime.parse("2020-03-11T23-50-39")
implicit val datetimeFormat= DateTimeFormat.forPattern("dd-MM-yyyy-HH-m");
"Little-Big--18-06-1999-23-12".split("--").toList match {
  case name::datetime::Nil=>LocalDateTime.parse(datetime/*s"${date} ${time/*.replace('-',':')*/}"*/,datetimeFormat)

}


