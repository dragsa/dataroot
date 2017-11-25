//name := "dataroot"
//
//version := "0.1"
//
//scalaVersion := "2.12.4"
//
//libraryDependencies ++= Seq(
//  "org.postgresql" % "postgresql" % "42.1.4",
//  "com.typesafe.slick" %% "slick" % "3.2.1",
//  "org.slf4j" % "slf4j-nop" % "1.6.4",
//  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
//)

lazy val root = Project(id = "slick_workshop", base = file(".")).settings(
  name := "dataroot",
  version := "0.1",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "org.postgresql" % "postgresql" % "42.1.4",
    "com.typesafe.slick" %% "slick" % "3.2.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
  )
).dependsOn(model)

lazy val model = Project("model", file("model"))