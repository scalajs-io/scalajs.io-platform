package io.scalajs.platform.tools.installer

import net.liftweb.json
import net.liftweb.json.DefaultFormats

import scala.io.Source

/**
  * Represents the Installer configuration
  * @param version the application version
  * @param repositories   the configured [[Repo repositories]]
  */
case class InstallerConfig(version: String, repositories: List[Repo])

/**
  * InstallerConfig Companion
  * @author lawrence.daniels@gmail.com
  */
object InstallerConfig {

  def apply(resourcePath: String): InstallerConfig = {
    implicit val formats: DefaultFormats = DefaultFormats
    val jsonString = Option(getClass.getResource(resourcePath))
      .map(Source.fromURL)
      .map(_.getLines().mkString)
      .getOrElse(throw new IllegalArgumentException(s"Configuration file '$resourcePath' was not found in the class path"))
    json.parse(jsonString).extract[InstallerConfig]
  }

}
