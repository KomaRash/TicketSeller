name := "Akka-WebShop-Dota2"

version := "0.1"

scalaVersion := "2.13.1"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked"

)
//Akka
val akkaVersion = "2.6.3"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"% akkaVersion,
  "com.typesafe.akka" %%  "akka-stream"% akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"  % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.9",
 // "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
"com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test",
"org.scalatest"     %% "scalatest"       % "3.1.0"       % "test"
)
//DataBase connection with ScalikeJDBC
libraryDependencies++=Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.4.0",
 "mysql" % "mysql-connector-java" % "8.0.19",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)
//Cats core
libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.0"
//add io.circle adapter akka-Json
libraryDependencies += "de.heikoseeberger" % "akka-http-circe_2.13" % "1.31.0"
val circeVersion = "0.12.3"
//Json Parsing with io.circle
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
"io.circe" %% "circe-generic",
"io.circe" %% "circe-parser"
).map(_ % circeVersion)
//DateTime
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc-joda-time" % "3.4.0"