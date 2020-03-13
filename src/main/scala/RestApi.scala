import EventOperations._
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.{ExecutionContext, Future}
class RestApi(system: ActorSystem, timeout: Timeout) extends BoxOfficeApi with JsonMarshaling {

  import akka.http.scaladsl.model.StatusCodes._
  override implicit def executionContext: ExecutionContext = system.dispatcher
  override implicit def requestTimeout: Timeout = timeout
  override def createBoxOffice(): ActorRef =system.actorOf(BoxOffice.property,BoxOffice.name)

  def eventsRoute=pathPrefix("events"){
    pathEndOrSingleSlash{
      get{
      onSuccess(getEvents()) {
        case GetEventsResponse(eventList) => complete(eventList)
        case CancelEventResponse=> complete(BadRequest)
      }
      }
    }
  }


  def routes:Route=eventsRoute
}

trait BoxOfficeApi{
  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout
  lazy val boxOffice: ActorRef = createBoxOffice()

  def createBoxOffice():ActorRef

  def createEvent(event:Event,tickets:Int,ticketType: TicketType): Future[EventResponse] =
    boxOffice.ask(CreateEvent(event,tickets,ticketType)).mapTo[EventResponse]

  def getEvents(): Future[EventResponse] =
    boxOffice.ask(GetEvents).mapTo[EventResponse]

  def getEvent(eventName: String): Future[EventResponse] =
    boxOffice.ask().mapTo[EventResponse]


}


