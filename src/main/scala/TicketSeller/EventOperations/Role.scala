package TicketSeller.EventOperations

import TicketSeller.EventOperations.AccessLevel._
import TicketSeller.EventOperations.User.{UserInfo, UserToken}
import akka.util.Timeout
import org.joda.time.{LocalDateTime, Period}

  sealed trait Role {
    def userRole:AccessLevel.AL= ???
}

case object Unauthorized extends Role{
  override def userRole: AccessLevel.Value = Anon

}
case class User(

                                    userNickName: String,
                                    userInfo: Option[UserInfo]=None,
                                    userToken: Option[UserToken]=None,
                                    accessLevel:AL=Authorized
                                  ) extends Role{

  override def userRole: AL = accessLevel
}
object User {
  type  Token=String
  case class UserToken(accessToken: Token, refreshToken:Option[Token], time: Option[LocalDateTime]){
     def valid(accessToken: Token)(implicit timeout: Timeout): Boolean ={
       val period=new Period(time.get,LocalDateTime.now()).toStandardSeconds.getSeconds.toLong
       println(period)
       println(timeout.duration.toSeconds)
      this.accessToken==accessToken &&   period<=timeout.duration.toSeconds

     }
  }

  case class UserInfo(userMail: String, password: Option[String] = None)

}
