package TicketSeller

import TicketSeller.EventOperations.EventOperations.{AuthorizeUserRequest, GetEvents}
import TicketSeller.EventOperations.UserInfo
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
object ActiveUsers {
  def property(implicit database:ActorRef): Props = Props(new ActiveUsers(database))

  def name="ActiveUsers"

}
class ActiveUsers(database:ActorRef) extends Actor with AuthorizeUserApi {


  override def receive: Receive = {
    case eventRequest:(GetEvents[_]) =>println(eventRequest.user.accessLevel)
    case authorizeUser:AuthorizeUserRequest[_]=>database.ask(authorizeUser.userInfo)
  }


}

trait AuthorizeUserApi extends AuthorizeTimeout {
  implicit val timeout: Timeout =userAskTimeout
  def generateToken(userInfo: UserInfo)= {

  }

}