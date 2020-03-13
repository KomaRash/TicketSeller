sealed trait  Role
case class Unauthorized() extends Role
case class User(userNickName:String,userInfo:Option[UserInfo]) extends Role
case class Admin(UserNickName:String,userInfo: Option[UserInfo]) extends Role
case class Redactor(UserNickName:String,userInfo: Option[UserInfo]) extends Role
case class UserInfo(userId:Int,userMail:String)
