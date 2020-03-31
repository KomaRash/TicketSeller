package TicketSeller.Codec

import TicketSeller.EventOperations.EventOperations.{Event, EventInfo, TicketSellerDateTime}
import TicketSeller.EventOperations.User.{UserInfo, UserToken}
import TicketSeller.EventOperations.Place
import org.joda.time.LocalDateTime

trait JsonCodec extends DateTimeCodec {
  import io.circe._
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.syntax._
  //Encoders
  implicit val eventEncoder: Encoder[Event] = deriveEncoder[Event].mapJsonObject(_.filter {
    case ("id" | "eventInfo" | "place" | "dateTime", value) => !value.isNull
    case _ => true
  })
  implicit val eventInfoEncoder: Encoder[EventInfo] = deriveEncoder[EventInfo]
  implicit val placeEncoder: Encoder[Place] = deriveEncoder[Place]
  lazy implicit val localDateTimeEncoder: Encoder[LocalDateTime] = (a: LocalDateTime) => a.toString(datetimeFormat).asJson
  lazy implicit val eventDateTimeEncoder: Encoder[TicketSellerDateTime] = deriveEncoder[TicketSellerDateTime]
  lazy implicit val userTokenEncoder:Encoder[UserToken]=deriveEncoder[UserToken]
  //Decoders
  lazy implicit val eventDecoder: Decoder[Event] = deriveDecoder[Event].prepare(prepareDecoders("id")).
    prepare(prepareDecoders("eventInfo")).
    prepare(prepareDecoders("dateTime"))
  lazy implicit val eventInfoDecoder: Decoder[EventInfo] = deriveDecoder[EventInfo]
  lazy implicit val placeDecoder: Decoder[Place] = deriveDecoder[Place]
  lazy implicit val localDateTimeDecoder: Decoder[TicketSellerDateTime] = (c: HCursor) => for {
    dateTime <- c.downField("datetime").as[String].map(LocalDateTime.parse(_, datetimeFormat))
  } yield TicketSellerDateTime(dateTime)
  lazy implicit val userInfoDecoder:Decoder[UserInfo]=deriveDecoder[UserInfo]
  //lazy implicit val userTokenDecoder:Decoder[UserToken]=deriveDecoder[UserToken]
  private def prepareDecoder(json: Json)(fieldType: String)(cursor: ACursor): ACursor = {
    val field = cursor.downField(fieldType)
    if (field.failed) {
      cursor.withFocus(_.mapObject(_.add(fieldType, json)))
    }
    else field.up
  }
  private val prepareDecoders = prepareDecoder(Json.Null) _

}
