package TicketSeller.EventOperations
object AccessLevel {
  type AccessLevel
  type Anon <: AccessLevel
  type Authorized <: Anon
  type Redactor <: Authorized
  type Admin <: Redactor

  type Token=String
}
import AccessLevel._

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._
sealed trait Role[+AL <: AccessLevel] {
  def accessLevel: Type = ???
}
case object Unauthorized extends Role[Anon]{
  override def accessLevel: universe.Type = typeOf[Anon]
}
  case class User[+AL <: Authorized](
                                    userNickName: String,
                                    userInfo: Option[UserInfo],
                                    userToken: Option[UserToken]
                                  ) extends Role[AL]{
    override def accessLevel: universe.Type = typeOf[Authorized]
  }

case class UserToken(accessToken:Token,refreshToken:Token)

case class UserInfo(userMail: String, password: String)

