package TicketSeller

import TicketSeller.EventOperations.{User, UserInfo}
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
object ActiveUsers {
  def property(implicit database:ActorRef): Props = Props(new ActiveUsers(database))

  def name="ActiveUsers"

}
class ActiveUsers(database:ActorRef) extends Actor with AuthorizeUserApi {

  private val activeUserList=List[User]()
  override def receive: Receive = {
    case userInfo: UserInfo=>database.ask(userInfo).mapTo[Option[User[_]]]
  }


}

trait AuthorizeUserApi extends AuthorizeTimeout {
  implicit val timeout: Timeout =userAskTimeout
  def generateToken(userInfo: UserInfo)= {

  }

}