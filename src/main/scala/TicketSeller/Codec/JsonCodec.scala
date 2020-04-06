package TicketSeller.Codec

import TicketSeller.EventOperations.Event
import TicketSeller.EventOperations.EventOperations.{EventInfo, TicketSellerDateTime}
import TicketSeller.EventOperations.Info.Place
import TicketSeller.EventOperations.User.{UserInfo, UserToken}
import org.joda.time.LocalDateTime

trait JsonCodec extends DateTimeCodec {
  import io.circe._
  import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
  import io.circe.syntax._

  /**
   * Implicit encoders for EventOperation Classes
   */
  lazy implicit val eventEncoder: Encoder[Event] = deriveEncoder[Event].mapJsonObject(_.filter {
    case ("id" | "eventInfo" | "place" | "dateTime", value) => !value.isNull
    case _ => true
  })
  lazy implicit val eventInfoEncoder: Encoder[EventInfo] = deriveEncoder[EventInfo]
  lazy implicit val placeEncoder: Encoder[Place] = deriveEncoder[Place]
  lazy implicit val localDateTimeEncoder: Encoder[LocalDateTime] = (a: LocalDateTime) => a.toString(datetimeFormat).asJson
  lazy implicit val eventDateTimeEncoder: Encoder[TicketSellerDateTime] = deriveEncoder[TicketSellerDateTime]

  /**
   * Implicit encoders for EventOperation classes
   */
  lazy implicit val userTokenEncoder:Encoder[UserToken]=deriveEncoder[UserToken]

  /**
   *  Implicit decoders for EventOperation classes
   */
  lazy implicit val eventDecoder: Decoder[Event] = deriveDecoder[Event].prepare(prepareDecoders("id")).
    prepare(prepareDecoders("eventInfo")).
    prepare(prepareDecoders("dateTime"))

  lazy implicit val eventInfoDecoder: Decoder[EventInfo] = deriveDecoder[EventInfo]

  lazy implicit val placeDecoder: Decoder[Place] = deriveDecoder[Place]

  lazy implicit val localDateTimeDecoder: Decoder[TicketSellerDateTime] = (c: HCursor) => for {
    dateTime <- c.downField("datetime").as[String].map(LocalDateTime.parse(_, datetimeFormat))
  } yield TicketSellerDateTime(dateTime)

  /**
   *  Implicit decoders for Role classes
   */
  lazy implicit val userInfoDecoder:Decoder[UserInfo]=deriveDecoder[UserInfo]

  private def prepareDecoder(json: Json)(fieldType: String)(cursor: ACursor): ACursor = {
    val field = cursor.downField(fieldType)
    if (field.failed) {
      cursor.withFocus(_.mapObject(_.add(fieldType, json)))
    }
    else field.up
  }

  private val prepareDecoders = prepareDecoder(Json.Null) _

}
