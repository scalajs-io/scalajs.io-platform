package io.scalajs.platform.tools.installer

import java.io.File

import io.scalajs.platform.tools.installer.ResourceHelper._
import org.slf4j.LoggerFactory

import scala.io.Source
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
  val rootDirectory: File = new File("./repos", name).getCanonicalFile

  /**
    * Project build file (e.g. "build.sbt")
    */
  val buildFile: File = new File(rootDirectory, "build.sbt")

  /**
    * Project build.properties file
    */
  val buildPropertiesFile: File = new File(rootDirectory, "project/build.properties")

  val nodeModulesDirectory: File = new File(rootDirectory, "node_modules")

  /**
    * Project package.json file
    */
  val packageJonFile: File = new File(rootDirectory, "package.json")

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
  def commit(message: String, fileNames: Seq[String]): Int =
    report(Process(List("git", "commit") ::: fileNames.toList ::: List("-m", message), cwd = rootDirectory).!, action = "Commit")

  /**
    * Compiles the code within repository
    * @param config the implicit [[InstallerConfig]]
    * @return the exit code
    */
  def compile()(implicit config: InstallerConfig): Int =
    report(Process(command = "sbt clean package", cwd = rootDirectory).!, action = "Compile")

  /**
    * Clones the code from the repository
    * @return the exit code
    */
  def download(): Int = report(s"git -C ${rootDirectory.getParent} clone https://github.com/scalajs-io/$name".!, action = "Download")

  /**
    * Clones (or pulls) the code from the repository
    * @return the exit code
    */
  def downloadOrUpdate(): Int = if (!rootDirectory.exists()) download() else update()

  def gitAdd(files: String*)(implicit config: InstallerConfig): Int =
    report(Process(command = s"git add ${files.mkString(" ")}", cwd = rootDirectory).!, action = s"git add ${files.mkString(" ")}")

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
    * Returns the list of files within the repository that have been modified
    * @return the list of modified files
    */
  def listModifiedFiles: List[String] = {
    val statusLabel = "modified:"
    Source.fromString(Process("git status", cwd = rootDirectory).!!).getLines().map(_.trim)
      .filter(_.startsWith(statusLabel))
      .map(_.drop(statusLabel.length).trim)
      .toList
  }

  def npmCheckUpdates(): Int = report(Process(command = "ncu -u", cwd = rootDirectory).!, action = "ncu -u")

  def npmInstall(): Int = report(Process(command = "npm install", cwd = rootDirectory).!, action = "npm install")

  /**
    * Publishes the code to local SBT repository
    * @return the exit code
    */
  def publishLocal(): Int = report(Process(command = "sbt publishLocal", cwd = rootDirectory).!, action = "publishLocal")

  /**
    * Pushes the commit on the repository
    * @return the exit code
    */
  def push(): Int = report(Process(command = "git push", cwd = rootDirectory).!, action = "push")

  /**
    * Performs a "soft" reset on the repository
    * @return the exit code
    */
  def reset(): Int = report(Process(command = "git reset --soft HEAD~1", cwd = rootDirectory).!, action = "reset")

  def test(): Int = report(Process(command = "sbt test", cwd = rootDirectory).!, action = "test")

  /**
    * Indicates whether the build file contains invalid entries
    * @return true, if the build file contains invalid entries
    */
  def unmatchedProject: Boolean =
    buildFile.exists() && !Source.fromFile(buildFile).use(_.mkString.contains(s"""name := "$name""""))

  /**
    * Pulls the updated code from the repository
    * @return the exit code
    */
  def update(): Int = report(s"git -C ./repos/$name pull".!, action = "pull")

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
