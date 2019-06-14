package io.scalajs.platform.tools.installer

import java.io.{File, FileWriter}

import io.scalajs.platform.tools.installer.ResourceHelper._

/**
  * File Generation Service
  * @author lawrence.daniels@gmail.com
  */
object FileGenerator {

  def createBuildFile(repo: CodeRepo)(implicit config: InstallerConfig): File = save(repo.buildFile) {
    s"""|import org.scalajs.sbtplugin.ScalaJSPlugin
        |import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
        |import sbt.Keys.{libraryDependencies, _}
        |import sbt._
        |
        |import scala.language.postfixOps
        |
        |val scalaJsIOVersion = "${config.version}"
        |val scalaJsVersion = "${config.scalaVersion}"
        |
        |homepage := Some(url("https://github.com/scalajs-io/${repo.name}"))
        |
        |lazy val root = (project in file(".")).
        |  enablePlugins(ScalaJSPlugin).
        |  settings(
        |    name := "${repo.name}",
        |    organization := "${repo.organization}",
        |    description := "An ${repo.name} binding for Scala.js",
        |    version := scalaJsIOVersion,
        |    scalaVersion := scalaJsVersion,
        |    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-Xlint"),
        |    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
        |    scalacOptions in(Compile, doc) ++= Seq("-no-link-warnings"),
        |    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
        |    autoCompilerPlugins := true,
        |    scalaJSModuleKind := ModuleKind.CommonJSModule,
        |    libraryDependencies ++= Seq(
        |${getDependencies(repo) mkString ",\n"}
        |  ))
        |
        |/////////////////////////////////////////////////////////////////////////////////
        |//      Publishing
        |/////////////////////////////////////////////////////////////////////////////////
        |
        |lazy val publishingSettings = Seq(
        |  sonatypeProfileName := "org.xerial",
        |  publishMavenStyle := true,
        |  publishTo := {
        |    val nexus = "https://oss.sonatype.org/"
        |    if (isSnapshot.value)
        |      Some("snapshots" at nexus + "content/repositories/snapshots")
        |    else
        |      Some("releases" at nexus + "service/local/staging/deploy/maven2")
        |  },
        |  pomExtra :=
        |    <url>https://github.com/scalajs-io/${repo.name}</url>
        |      <licenses>
        |        <license>
        |          <name>MIT License</name>
        |          <url>http://www.opensource.org/licenses/mit-license.php</url>
        |        </license>
        |      </licenses>
        |      <scm>
        |        <connection>scm:git:github.com/scalajs-io/${repo.name}.git</connection>
        |        <developerConnection>scm:git:git@github.com:scalajs-io/${repo.name}.git</developerConnection>
        |        <url>github.com/scalajs-io/${repo.name}.git</url>
        |      </scm>
        |      <developers>
        |        <developer>
        |          <id>ldaniels528</id>
        |          <name>Lawrence Daniels</name>
        |          <email>lawrence.daniels@gmail.com</email>
        |          <organization>io.scalajs</organization>
        |          <organizationUrl>https://github.com/scalajs-io</organizationUrl>
        |          <roles>
        |            <role>Project-Administrator</role>
        |            <role>Developer</role>
        |          </roles>
        |          <timezone>+7</timezone>
        |        </developer>
        |      </developers>
        |)
        |
        |// loads the Scalajs-io root project at sbt startup
        |onLoad in Global := (Command.process("project root", _: State)) compose (onLoad in Global).value
        |""".stripMargin
  }

  def createBuildPropertiesFile(repo: CodeRepo)(implicit config: InstallerConfig): File = save(repo.buildPropertiesFile) {
    s"sbt.version=${config.sbtVersion}"
  }

  def createPackageJsonFile(repo: CodeRepo)(implicit config: InstallerConfig): File = save(repo.packageJonFile) {
    s"""|{
        |  "name": "${repo.name}-sjs",
        |  "version": "${config.version}",
        |  "private": true,
        |  "dependencies": {
        |    "source-map-support": "^0.5.12"
        |  },
        |  "engines": {
        |    "node": ">=0.12"
        |  }
        |}
        |""".stripMargin('|')
  }

  def createPluginsFile(repo: CodeRepo)(implicit config: InstallerConfig): File = save(repo.pluginsFile) {
    s"""|// Scala.js
        |
        |addSbtPlugin("org.scala-js" % "sbt-scalajs" % "${config.scalaJsVersion}")
        |
        |// Publishing
        |
        |addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")
        |
        |addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")
        |
        |// Resolvers
        |
        |resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
        |resolvers += Resolver.url("scala-js-snapshots", url("http://repo.scala-js.org/repo/snapshots/"))(Resolver.ivyStylePatterns)
        |""".stripMargin('|')
  }

  def createReadMeFile(repo: CodeRepo)(implicit config: InstallerConfig): File = save(repo.readMeFile) {
    s"""|${repo.name.capitalize} API for Scala.js
        |================================
        |[${repo.name}](https://www.npmjs.com/package/${repo.name}) - **TODO**
        |
        |### Description
        |
        |**TODO**
        |
        |### Build Dependencies
        |
        |* [SBT v${config.sbtVersion}](http://www.scala-sbt.org/download.html)
        |
        |### Build/publish the SDK locally
        |
        |```bash
        | $$ sbt clean publish-local
        |```
        |
        |### Running the tests
        |
        |Before running the tests the first time, you must ensure the npm packages are installed:
        |
        |```bash
        | $$ npm install
        |```
        |
        |Then you can run the tests:
        |
        |```bash
        | $$ sbt test
        |```
        |
        |### Examples
        |
        |**TODO**
        |
        |### Artifacts and Resolvers
        |
        |To add the `${repo.name.capitalize}` binding to your project, add the following to your build.sbt:
        |
        |```sbt
        |libraryDependencies += "${repo.organization}" %%% "${repo.name}" % "${config.version}"
        |```
        |
        |Optionally, you may add the Sonatype Repository resolver:
        |
        |```sbt
        |resolvers += Resolver.sonatypeRepo("releases")
        |```
        |""".stripMargin
  }

  def getDependencies(repo: CodeRepo)(implicit config: InstallerConfig): List[String] = {
    val mapping = Map(config.repositories.map(repo => repo.name -> repo): _*)
    val dependencies = for {
      name <- repo.dependencies
      dependency <- mapping.get(name)
    } yield
      s"""\t\t"${dependency.organization}" %%% "${dependency.name}" % scalaJsIOVersion"""

    dependencies ::: List(
      s""""org.scala-lang" % "scala-reflect" % "${config.scalaVersion}"""",
      s""""org.scalatest" %%% "scalatest" % "3.0.1" % "test""""
    )
  }

  def save(file: File)(content: => String): File = {
    file.getParentFile.mkdirs()
    new FileWriter(file) use (_.write(content))
    file
  }

}
