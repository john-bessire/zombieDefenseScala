name := "zombieDefenseScala"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.twitter4j"% "twitter4j-core"% "4.0.1",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  jdbc,
  anorm,
  cache
)  

play.Project.playScalaSettings
