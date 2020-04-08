package TicketSeller
import TicketSeller.Codec.{JsonCodec, UriDecoder}
import TicketSeller.EventOperations.User.{Token, UserInfo, UserToken}
import TicketSeller.EventOperations.{AuthorizeUserResponse, CancelEventResponse, Event, EventResponse, GetEvent, GetEventResponse, GetEvents, GetEventsResponse, RefreshToken, Role, Unauthorized, User}
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

  override implicit def executionContext: ExecutionContext =system.dispatcher
  override implicit def requestTimeout: Timeout = timeout
  override def createBoxOffice(): ActorRef =system.actorOf(BoxOffice.property(requestTimeout),BoxOffice.name)

  def eventsRoute=pathPrefix("events") {
    pathEndOrSingleSlash {
      get {
        onSuccess(getEvents(Unauthorized)) {
          case GetEventsResponse(eventList,_) => complete(eventList)
          case CancelEventResponse(message,_) => complete(message)
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
        entity(as[UserInfo]) { userInfo => onSuccess(authorizeUser(userInfo)){
          case CancelEventResponse(message, _)=>complete(message)
          case AuthorizeUserResponse(user) =>complete(user.userToken)
        }
        }
      }
    }
  }
  def refreshTokenRoute=pathPrefix("refresh"){
    {
      pathEndOrSingleSlash{
        get{
          entity(as[RefreshToken]){
            tokens=>onSuccess(refreshToken(tokens))(value => complete(value))
          }
        }
      }
    }
  }

  def testTokenRoute=pathPrefix("users"/Segment){
    token=>pathEndOrSingleSlash{
      get{
        onSuccess(userAuthentication(token) ){
          case Some(user)=>complete(user.userToken)
          case None=>complete("Not Authenticate")
        }
      }
    }
  }

  def routes:Route= eventsRoute~
                    eventRoute~
                    authorizeRoute~
                    testTokenRoute~
                    refreshTokenRoute
}

trait BoxOfficeApi{
  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout
  lazy val boxOffice: ActorRef = createBoxOffice()

  def createBoxOffice():ActorRef

  def getEvents(user:Role): Future[EventResponse] =
    boxOffice.ask(GetEvents(user)).mapTo[EventResponse]

  def getEvent(event: Either[String,Event],user:Role): Future[EventResponse] = {
    event match {
      case Right(value) => boxOffice.ask(GetEvent(value,user)).mapTo[EventResponse]
      case Left(value) =>  Future{CancelEventResponse(value,user)}
    }
   }
  def authorizeUser(userInfo:UserInfo): Future[EventResponse] ={
    boxOffice.ask(userInfo).mapTo[EventResponse]
  }
  def userAuthentication(token: Token): Future[Option[User]] ={
    boxOffice.ask(token).mapTo[Option[User]]
  }
  def refreshToken(refreshToken: RefreshToken): Future[Option[UserToken]] =boxOffice.
    ask(refreshToken).
    mapTo[Option[UserToken]]


}


