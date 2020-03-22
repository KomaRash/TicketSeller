package TicketSeller
import TicketSeller.EventOperations.AccessLevel.{AccessLevel, Authorized}
import TicketSeller.EventOperations.EventOperations._
import TicketSeller.EventOperations.{Role, Unauthorized, User, UserInfo}
import TicketSeller.Codec.{JsonCodec, UriDecoder}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.{ExecutionContext, Future}

class RestApi( system: ActorSystem, timeout: Timeout) extends BoxOfficeApi
                                                      with JsonCodec
                                                      with UriDecoder {
  override implicit def executionContext: ExecutionContext = system.dispatcher
  override implicit def requestTimeout: Timeout = timeout
  override def createBoxOffice(): ActorRef =system.actorOf(BoxOffice.property(system,requestTimeout),BoxOffice.name)

  def eventsRoute=pathPrefix("events") {
    pathEndOrSingleSlash {
      get {
        onSuccess(getEvents(Unauthorized)) {
          case GetEventsResponse(eventList,user) => complete(eventList)
          case CancelEventResponse(message,user) => complete(message)
        }
      }
    }
  }
  def eventRoute=pathPrefix("events"/Segment){
    event=>
      pathEndOrSingleSlash{
        get{
          onSuccess(getEvent(fromUriToEvent(event),Unauthorized)) {
            case GetEventResponse(event,user) => complete(event)
            case CancelEventResponse(message,user) => complete(message)
          }
        }
      }
    }
  def authorizeRoute=pathPrefix("users"){
    pathEndOrSingleSlash{
      get{
        entity(as[User[Authorized]]){
            user=>authorizeUser(user)
        }
      }
    }
  }



  def routes:Route=eventsRoute~eventRoute
}

trait BoxOfficeApi{
  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout
  lazy val boxOffice: ActorRef = createBoxOffice()

  def createBoxOffice():ActorRef

  def getEvents[AL <: AccessLevel](user:Role[AL]): Future[EventResponse[AL]] =
    boxOffice.ask(GetEvents(user)).mapTo[EventResponse[AL]]

  def getEvent[AL <: AccessLevel](event: Either[String,Event],user:Role[AL]): Future[EventResponse[AL]] = {
    event match {
      case Right(value) => boxOffice.ask(GetEvent(value,user)).mapTo[EventResponse[AL]]
      case Left(value) =>  Future{CancelEventResponse(value,user)}
    }
   }
  def authorizeUser(user:User[Authorized])={
    user.userInfo match {
      case Some(userInfo) => boxOffice.ask(AuthorizeUserRequest(userInfo))
      case None =>
    }
  }


}


