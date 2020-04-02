package TicketSeller.EventOperations

import TicketSeller.EventOperations.AccessLevel._
import TicketSeller.EventOperations.User.{Token, UserInfo, UserToken}
import akka.util.Timeout
import cats.implicits._
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

  def confirmAccessToken(newAccessToken:Token)(implicit timeout: Timeout, accessToken: Token): (Option[User], Boolean) = {
    if (!userToken.exists(_.validTime)) (None,false)
    else
      if(userToken.exists(_.validAccessToken)) (this.copy(userToken = userToken.map(_.copy(accessToken=newAccessToken))).some,true)
    else (this.some,false)
  }
      override def userRole: AL = accessLevel
}
object User {

  type  Token=String
  case class UserToken(accessToken: Token, refreshToken:Option[Token], time: Option[LocalDateTime]){
       def userTimePeriod: Long = {
         new Period(time.get,LocalDateTime.now()).
         toStandardSeconds.
         getSeconds.
         toLong
       }
      def validAccessToken(implicit timeout: Timeout, accessToken: Token): Boolean = (this.accessToken==accessToken)
      def validTime(implicit timeout: Timeout, accessToken: Token): Boolean =userTimePeriod<=timeout.duration.toSeconds


  }

  case class UserInfo(userMail: String, password: Option[String] = None)

}
