package TicketSeller

import TicketSeller.EventOperations.User.UserInfo
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
object ActiveUsers {
  def property(implicit database:ActorRef): Props = Props(new ActiveUsers(database))
  def name="ActiveUsers"

}
class ActiveUsers(database:ActorRef) extends Actor with AuthorizeUserApi {
  import akka.pattern.{ask, pipe}
  //implicit val a=ExecutionContext.global
  implicit val b=context.system.dispatcher
//  private val activeUserList=List[User]()
  override def receive: Receive = {
    case userInfo: UserInfo=> database.ask(userInfo) pipeTo sender()
  }


}

trait AuthorizeUserApi extends AuthorizeTimeout {
  implicit val timeout: Timeout =userAskTimeout
  def generateToken(userInfo: UserInfo)= {

  }

}