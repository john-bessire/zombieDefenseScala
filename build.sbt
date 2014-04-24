name := "zombieDefenseScala"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.twitter4j"% "twitter4j-core"% "4.0.1",
  jdbc,
  anorm,
  cache
)  

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.1"

play.Project.playScalaSettings
