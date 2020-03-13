
import EventOperations._
import io.circe.Encoder
import io.circe.generic.semiauto._
trait JsonMarshaling {
  implicit val eventEncoder:Encoder[Event]=deriveEncoder[Event].mapJsonObject(_.filter{
    case ("id" | "eventInfo"|"place"|"dateTime",value)=> !value.isNull
    case _=>true
  })
  implicit val eventInfoEncoder:Encoder[EventInfo]=deriveEncoder[EventInfo]
  implicit val placeEncoder:Encoder[Place]=deriveEncoder[Place]

}
