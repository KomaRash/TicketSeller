package TicketSeller.Operations
object AccessLevel {
  type AccessLevel
  type Anon <: AccessLevel
  type Authorized <: Anon
  type Redactor <: Authorized
  type Admin <: Redactor
}
import AccessLevel._

sealed trait Role[+AL <: AccessLevel]
case object Unauthorized extends Role[Anon]
case class User[+AL <: Authorized](
                                    userNickName: String,
                                    userInfo: Option[UserInfo]
                                  ) extends Role[AL]

case class UserInfo(userId: Int, userMail: String, password: String)

