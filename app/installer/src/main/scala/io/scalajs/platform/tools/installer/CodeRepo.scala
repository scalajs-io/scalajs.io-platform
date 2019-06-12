package io.scalajs.platform.tools.installer

import java.io.File

import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}
import scala.sys.process._
import scala.util.Properties.userHome

/**
  * Represents a GitHub code repository
  * @param name         the name of the GitHub repository
  * @param organization the repository's organization
  * @param dependencies the repository's dependencies
  */
case class CodeRepo(name: String, organization: String, dependencies: List[String]) {
  private lazy val logger = LoggerFactory.getLogger(getClass)

  /**
    * Root directory of the repository
    */
  val rootDirectory: File = new File("./repos", name)

  /**
    * Project build file (e.g. "build.sbt")
    */
  val buildFile: File = new File(rootDirectory, "build.sbt")

  /**
    * Project build.properties file
    */
  val buildPropertiesFile: File = new File(rootDirectory, "project/build.properties")

  /**
    * Project plugins.sbt file
    */
  val pluginsFile: File = new File(rootDirectory, "project/plugins.sbt")

  /**
    * Project README.md file
    */
  val readMeFile: File = new File(rootDirectory, "README.md")

  /**
    * Commits the code to the repository
    * @param message the commit message
    * @return the exit code
    */
  def commit(message: String): Int = {
    logger.info(s"Committing '$name'...")
    report(Process(command = s"""git commit -a -m "$message" """, cwd = rootDirectory).!, action = "Commit")
  }

  /**
    * Compiles the code within repository
    * @param config the implicit [[InstallerConfig]]
    * @return the exit code
    */
  def compile()(implicit config: InstallerConfig): Int = {
    logger.info(s"Compiling '$name'...")
    report(Process(command = "sbt clean compile", cwd = rootDirectory).!, action = "Compile")
  }

  /**
    * Clones the code from the repository
    * @return the exit code
    */
  def download(): Int = {
    logger.info(s"Downloading '$name'...")
    report(s"git -C ${rootDirectory.getParent} clone https://github.com/scalajs-io/$name".!, action = "Download")
  }

  /**
    * Clones (or pulls) the code from the repository
    * @return the exit code
    */
  def downloadOrUpdate(): Int = if (!rootDirectory.exists()) download() else update()

  /**
    * Indicates whether the repository's dependencies have been satisfied
    * @param ctx the implement [[ProcessingContext]]
    * @return true, if all of the repository's dependencies have been satisfied
    */
  def isSatisfied(implicit ctx: ProcessingContext): Boolean = dependencies.isEmpty || dependencies.forall(ctx.isCompleted)

  /**
    * Returns the published jar file
    * @param config the implement [[InstallerConfig]]
    * @return the [[File]]
    */
  def ivy2File(implicit config: InstallerConfig): File =
    new File(userHome, s".ivy2/local/$organization/${name}_sjs0.6_2.12/${config.version}/jars/${name}_sjs0.6_2.12.jar")

  /**
    * Returns the packaged jar file
    * @param config the implement [[InstallerConfig]]
    * @return the [[File]]
    */
  def jarFile(implicit config: InstallerConfig): File =
    new File(rootDirectory, s"target/scala-2.12/${name}_sjs0.6_2.12-${config.version}.jar")

  /**
    * Publishes the code to local SBT repository
    * @return the exit code
    */
  def publishLocal()(implicit config: InstallerConfig): Int = {
    logger.info(s"Publishing local '$name'...")
    report(Process(command = "sbt publishLocal", cwd = rootDirectory).!, action = "PublishLocal")
  }

  /**
    * Indicates whether the build file contains invalid entries
    * @return true, if the build file contains invalid entries
    */
  def unmatchedProject: Boolean = {
    def closer[A](bs: BufferedSource)(f: BufferedSource => A): A = try f(bs) finally bs.close()

    buildFile.exists() && !closer(Source.fromFile(buildFile))(_.mkString.contains(s"""name := "$name""""))
  }

  /**
    * Pulls the updated code from the repository
    * @return the exit code
    */
  def update(): Int = {
    logger.info(s"Updating '$name'...")
    report(s"git -C ./repos/$name pull".!, action = "Update")
  }

  /**
    * Reports failed exit codes
    * @param exitCode the given exit code
    * @param action   the action being executed at the time of failure
    * @return the exit code
    */
  private def report(exitCode: Int, action: String): Int = {
    if (exitCode != 0)
      logger.warn(s"$name: $action ended with exit code $exitCode")
    exitCode
  }

}
