import sbt.Keys.{libraryDependencies, _}
import sbt._

import scala.language.postfixOps

val appVersion = "0.1.0"
val scalaJsVersion = "2.12.8"
val slf4jVersion = "1.7.25"

homepage := Some(url("https://github.com/scalajs-io/node-platform-builder"))

lazy val installer = (project in file("./app/installer")).
  settings(
    name := "node-platform-builder",
    organization := "io.scalajs.npm",
    description := "Node.js Platform for Scala.js",
    version := appVersion,
    scalaVersion := scalaJsVersion,
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-Xlint"),
    scalacOptions in(Compile, doc) ++= Seq("-no-link-warnings"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.23",
      "log4j" % "log4j" % "1.2.17",
      "net.liftweb" %% "lift-json" % "3.1.1",
      "org.slf4j" % "slf4j-api" % slf4jVersion,
      "org.slf4j" % "slf4j-log4j12" % slf4jVersion,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ))

/////////////////////////////////////////////////////////////////////////////////
//      Publishing
/////////////////////////////////////////////////////////////////////////////////

lazy val publishingSettings = Seq(
  sonatypeProfileName := "org.xerial",
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra :=
    <url>https://github.com/scalajs-io/express</url>
      <licenses>
        <license>
          <name>MIT License</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:github.com/scalajs-io/express.git</connection>
        <developerConnection>scm:git:git@github.com:scalajs-io/express.git</developerConnection>
        <url>github.com/scalajs-io/express.git</url>
      </scm>
      <developers>
        <developer>
          <id>ldaniels528</id>
          <name>Lawrence Daniels</name>
          <email>lawrence.daniels@gmail.com</email>
          <organization>io.scalajs</organization>
          <organizationUrl>https://github.com/scalajs-io</organizationUrl>
          <roles>
            <role>Project-Administrator</role>
            <role>Developer</role>
          </roles>
          <timezone>+7</timezone>
        </developer>
      </developers>
)

// loads the Scalajs-io root project at sbt startup
onLoad in Global := (Command.process("project installer", _: State)) compose (onLoad in Global).value
