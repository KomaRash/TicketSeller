import EventOperations._
import Main.config
import akka.actor.{Actor, Props}
import scalikejdbc._ // for Functor import cats.syntax.functor.__
object Database extends DatabaseRowCoder {

  def name: String = "ticketsDatabase"
  def property: Props = Props(new Database)
  val jdbc: String = config.getString("Database.url")
  val passwordDB: String = config.getString("Database.password")
  val userDB: String = config.getString("Database.user")

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton(jdbc,
    userDB, passwordDB)
  implicit val session: DBSession = AutoSession

}
class Database() extends Actor{
  import Database._
  def getEvents= GetEventsResponse(sql""" select * from event""".map(_.toEventWithoutInfo()).list().apply())
  def getEvent(event: Event)=GetEventResponse(
    sql""" select * from event natural join place where DATETIME=${event.dateTime}
         and Name=${event.name}""".map(_.toUserEventInfo()).single().apply().get)

  override def receive: Receive = {
  //  case CreateEvent(event,tickets,ticketType)=>sender()! addEvent(event,ticketType,tickets)
    case GetEvent(event)=>sender() ! getEvent(event)
    case GetEvents=>sender() ! getEvents
  }
}
