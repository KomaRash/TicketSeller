package TicketSeller

import java.util.UUID
import cats.implicits._
import TicketSeller.EventOperations.EventOperations.{AuthorizeUserResponse, CancelEventResponse}
import TicketSeller.EventOperations.User
import TicketSeller.EventOperations.User.{Token, UserInfo, UserToken}
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import org.joda.time.LocalDateTime

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
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
      userResponse pipeTo sender() /*println(1)*/
      userResponse.mapTo[AuthorizeUserResponse].onComplete {
        case Failure(exception) => /*println("userNotcompleteAUth")*/
        case Success(value) =>
          context.become(onMessage(value.user::userList))
          //userList.foreach(println)

      }
    case userToken: Token=>sender()! userList.find{
      user=>user.
            userToken.
            exists{
              _.valid(userToken)(refreshTokenTimeout)
            }
    }

  }
}

trait AuthorizeUserApi extends AuthorizeTimeout {
  val refreshTokenTimeout:Timeout=userRefreshTokenTimeout
  implicit val timeout: Timeout =userAskTimeout
  def getUserToken:UserToken=UserToken(generateToken,generateToken.some,LocalDateTime.now().some)
  def generateToken: Token=UUID.randomUUID().toString

}