name := "zombieDefenseScala"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.twitter4j"% "twitter4j-core"% "4.0.1",
  jdbc,
  anorm,
  cache
)  

play.Project.playScalaSettings
