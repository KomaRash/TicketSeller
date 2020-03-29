package TicketSeller.EventOperations
object AccessLevel {
  type AccessLevel
  type Anon <: AccessLevel
  type Authorized <: Anon
  type Redactor <: Authorized
  type Admin <: Redactor

  type Token=String
}
import java.time.LocalDateTime

import TicketSeller.EventOperations.AccessLevel._

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

  sealed trait Role[AL <: AccessLevel] {
  def accessLevel:TypeTag[AL]= ???
}

case object Unauthorized extends Role[Anon]{
  override def accessLevel: universe.TypeTag[Anon] = typeTag[Anon]
}
case class User[AL <: Authorized](
                                    userNickName: String,
                                    userInfo: Option[UserInfo]=None,
                                    userToken: Option[UserToken]=None,
                                    userRole:TypeTag[AL]
                                  ) extends Role[AL]{
  override def accessLevel: universe.TypeTag[AL] = typeTag[AL]
}

case class UserToken(accessToken:Token,refreshToken:Token,time:LocalDateTime)

case class UserInfo(userMail: String, password: Option[String]=None)

