package TicketSeller.EventOperations

import java.time.LocalDateTime

import TicketSeller.EventOperations.AccessLevel._
import TicketSeller.EventOperations.User.{UserInfo, UserToken}

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
  case class UserToken(accessToken: Token, refreshToken: Token, time: LocalDateTime)

  case class UserInfo(userMail: String, password: Option[String] = None)

}
