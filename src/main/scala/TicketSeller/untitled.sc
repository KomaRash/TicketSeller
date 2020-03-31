

object AccessLevel extends Enumeration {
  type AL = Value
  val Anon:AL= Value(0,"Anon")
  val Authorized:AL= Value(1,"Authorized")
  val Redactor:AL = Value(2,"Redactor")
  val Admin:AL = Value(3,"Admin")
}
AccessLevel.Anon.id
