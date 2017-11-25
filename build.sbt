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

val globalSettings = Seq[SettingsDefinition](
  version := "0.1",
  scalaVersion := "2.12.4"
)

lazy val root = Project(id = "slick_workshop", base = file("."))
  .settings(globalSettings: _*)
  .aggregate(application)

lazy val repositories = Project("repos", file("repos"))
  .settings(globalSettings: _*)
  .settings(
    name := "dataroot_slick_wrokshop",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.1.4",
      "com.typesafe.slick" %% "slick" % "3.2.1",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
    )
  )
  .dependsOn(model)

lazy val model = Project("model", file("model")).settings(globalSettings: _*)

lazy val application = Project("application", file("application"))
  .settings(globalSettings: _*)
  .dependsOn(repositories)
//  .dependsOn(repositories, depProject)

//lazy val depProject = RootProject(uri("https://github.com/dragsa/AlgorithmsPart_1_new.git#master"))
