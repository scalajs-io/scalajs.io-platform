package io.scalajs.platform.tools.installer

import net.liftweb.json
import net.liftweb.json.DefaultFormats

import scala.io.Source

/**
  * Represents the Installer configuration
  * @param sbtVersion     the SBT version (e.g. "1.2.8")
  * @param scalaJsVersion the ScalaJs Version (e.g. "0.6.28")
  * @param scalaVersion   the Scala version (e.g. "2.12.8")
  * @param version        the application version (e.g. "0.5.0")
  * @param repositories   the configured [[CodeRepo repositories]]
  */
case class InstallerConfig(sbtVersion: String,
                           scalaJsVersion: String,
                           scalaVersion: String,
                           version: String,
                           repositories: List[CodeRepo])

/**
  * InstallerConfig Companion
  * @author lawrence.daniels@gmail.com
  */
object InstallerConfig {

  /**
    * Creates a new installer configuration
    * @param resourcePath the given resource path
    * @return new [[InstallerConfig installer configuration]]
    */
  def apply(resourcePath: String): InstallerConfig = {
    implicit val formats: DefaultFormats = DefaultFormats
    val jsonString = Option(getClass.getResource(resourcePath))
      .map(Source.fromURL)
      .map(_.getLines().mkString)
      .getOrElse(throw new IllegalArgumentException(s"Configuration file '$resourcePath' was not found in the class path"))
    json.parse(jsonString).extract[InstallerConfig]
  }

}
