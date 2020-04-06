package TicketSeller

import java.util.UUID

import TicketSeller.EventOperations.{AuthorizeUserResponse, CancelEventResponse, User}
import TicketSeller.EventOperations.User.{Token, UserInfo, UserToken}
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import cats.implicits._
import org.joda.time.LocalDateTime

import scala.annotation.tailrec
import scala.concurrent.ExecutionContextExecutor
import scala.util.Success
object ActiveUsers {
  def property(implicit database:ActorRef): Props = Props(new ActiveUsers(database))
  def name="ActiveUsers"

}
class ActiveUsers(database:ActorRef) extends Actor with AuthorizeUserApi {
  import akka.pattern.{ask, pipe}

  private implicit val executor: ExecutionContextExecutor =context.system.dispatcher
  private val ActiveUserList: List[User] = List[User]()
  override def receive: Receive = onMessage(ActiveUserList)

  private def onMessage(userList: List[User]): Receive = {
    case userInfo: UserInfo =>
      val userResponse = database.ask(userInfo).map{
        case authorizeUserResponse: AuthorizeUserResponse=> authorizeUserResponse.copy(
          user=authorizeUserResponse.user.copy(userToken = getUserToken.some))
        case cancelEventResponse: CancelEventResponse=>cancelEventResponse.copy(message = "User not authorize")
      }
      userResponse pipeTo sender()
      userResponse.mapTo[AuthorizeUserResponse].onComplete {
        /*case Failure(exception) => println("userNotcompleteAUth")*/
        case Success(value) =>
          context.become(onMessage(value.user::userList))

      }
    case authenticateToken: Token=>sender()! {
      val (currentUser,newUserList)=authenticateUser(userList)(refreshTokenTimeout,authenticateToken)
      context.become(onMessage(newUserList))
      currentUser
    }

  }
}

trait AuthorizeUserApi extends AuthorizeTimeout {
  val refreshTokenTimeout:Timeout=userRefreshTokenTimeout
  implicit val timeout: Timeout =userAskTimeout
  def getUserToken:UserToken=UserToken(generateToken,generateToken.some,LocalDateTime.now().some)
  def generateToken:Token=UUID.randomUUID().toString
  def authenticateUser(userList:List[User])
                      (implicit refreshTokenTimeout: Timeout,accessToken: Token): (Option[User], List[User]) ={
    @tailrec
    def tailRecM(tailUserList:List[User],accUserList:List[User]=List[User]()):(Option[User],List[User])= {
      if (tailUserList.isEmpty)
        (None,accUserList)
      else {
        val currentUser = tailUserList.head
        val (userOpt, current) = currentUser.confirmAccessToken(generateToken)(refreshTokenTimeout, accessToken)
        if (!current)
          tailRecM(tailUserList.tail, userOpt.toList ::: accUserList)
        else
          (userOpt, userOpt.toList ::: accUserList ::: tailUserList.tail)
      }
    }
    if(userList.isEmpty)
      (None,List())
    else
      tailRecM(userList)
  }

}