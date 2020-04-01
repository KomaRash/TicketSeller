

object AccessLevel extends Enumeration {
  type AL = Value
  val Anon:AL= Value(0,"Anon")
  val Authorized:AL= Value(1,"Authorized")
  val Redactor:AL = Value(2,"Redactor")
  val Admin:AL = Value(3,"Admin")
}
AccessLevel.Anon.id
import cats.kernel.Eq
// import cats.kernel.Eq

import cats.implicits._
// import cats.implicits._

case class Foo(a: Int, b: String)
// defined class Foo

implicit val eqFoo: Eq[Foo] = Eq.fromUniversalEquals
// eqFoo: cats.kernel.Eq[Foo] = cats.kernel.Eq$$anon$6@603461a4

//Foo(10, "") === Option(Foo(10, ""))
// res4: Boolean = true
Foo(10, "").some.exists(f=>f.equals(Foo(10, "")))