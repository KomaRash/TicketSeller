import TicketSeller.EventOperations.AccessLevel.{AccessLevel, Admin, Anon}
import TicketSeller.EventOperations.EventOperations.{EventRequest, GetEvents}
import TicketSeller.EventOperations.User

import scala.reflect.runtime.universe._

{
  def processMap[T: TypeTag](json: Map[String, T]) {
    typeOf[T] match {
      case t if t =:= typeOf[String] =>
        println("Map of strings")
      case t if t <:< typeOf[List[Int]] =>
        println("Map of list of foos")
    }
  }

  processMap(Map("aaa" -> "bbb"))
  processMap(Map("ccc" -> List(1,312)))
}
def process[T<:AccessLevel](request:EventRequest[T])(implicit tag:TypeTag[T]): Unit =
{
  typeOf[T] match {
    case t if t=:= typeOf[AccessLevel]=>println("2")
    case t if t=:= typeOf[Anon]=>println("1")
    case t if t=:= typeOf[Admin]=>println("Adm")
    case _=>println(123)
  }
}
process(GetEvents(User[Admin]("dasd",None)))










