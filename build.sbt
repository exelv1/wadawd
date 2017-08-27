
name := """gesn-ui"""

organization := "net.gesn"

scalaVersion := "2.11.6"

// Dependencies

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41" exclude("org.slf4j", "slf4j-simple"),
  "mysql" % "mysql-connector-java" % "5.1.34",
  "commons-io" % "commons-io" % "2.3",
  "org.apache.httpcomponents" % "httpclient" % "4.3.5",
  "org.webjars" % "bootstrap" % "3.3.2" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "font-awesome" % "4.5.0",
  "org.webjars" % "bootstrap-slider" % "4.8.3",
  "c3p0" % "c3p0" % "0.9.1.2"
)

// Plugins

lazy val root = (project in file(".")).
      enablePlugins(PlayScala)


packageSummary := "GESN UI"

packageDescription := "GESN UI"

rpmVendor := "gesn.net"

rpmRelease := (if(version.value.contains("-SNAPSHOT")) "SNAPSHOT" else "RELEASE")

version in Rpm := version.value.replaceAll("-SNAPSHOT", "")

rpmLicense := Some("GESN LICENSE")

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys ++= Seq[BuildInfoKey](
  name,
  version,
  scalaVersion,
  sbtVersion,
  resolvers,
  libraryDependencies in Test,
  BuildInfoKey.map(name) { case (k, v) => "project" + k.capitalize -> v.capitalize },
  BuildInfoKey.action("buildTime") {
    System.currentTimeMillis
  } // re-computed each time at compile
)