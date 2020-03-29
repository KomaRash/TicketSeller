import TicketSeller.EventOperations.AccessLevel.{AccessLevel, Admin, Authorized}

import scala.reflect.runtime.universe._

sealed trait Role[AL <: AccessLevel] {
}
case class User[AL <: Authorized](
                                   userNickName: String,
                                   userRole:TypeTag[AL]
                                 ) extends Role[AL]{
 }
User(userNickName="dasda",userRole=typeTag[Admin]).userRole


