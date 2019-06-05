package io.scalajs.platform.tools.installer

import java.io.File

import org.slf4j.LoggerFactory

import scala.io.{BufferedSource, Source}
import scala.sys.process._
import scala.util.Properties

/**
  * Represents a GitHub repository
  * @param name         the name of the GitHub repository
  * @param organization the repository's organization
  * @param dependencies the repository's dependencies
  */
case class Repo(name: String, organization: String, dependencies: List[String]) {
  private lazy val logger = LoggerFactory.getLogger(getClass)

  val repoDir = new File("./repos", name)

  val buildFile = new File(repoDir, "build.sbt")

  def commit(message: String): Int = {
    logger.info(s"Committing '$name'...")
    report(Process(command = s"""git commit -a -m "$message" """, cwd = repoDir).!, action = "Commit")
  }

  def compile()(implicit config: InstallerConfig): Int = {
    logger.info(s"Compiling '$name'...")
    report(Process(command = "sbt clean compile", cwd = repoDir).!, action = "Compile")
  }

  def download(): Int = {
    logger.info(s"Downloading '$name'...")
    report(s"git -C ${repoDir.getParent} clone https://github.com/scalajs-io/$name".!, action ="Download")
  }

  def downloadOrUpdate(): Int = if (!repoDir.exists()) download() else update()

  def isSatisfied(implicit ctx: ProcessingContext): Boolean = dependencies.isEmpty || dependencies.forall(ctx.isCompleted)

  def jarFile(implicit config: InstallerConfig) = new File(repoDir, s"target/scala-2.12/${name}_sjs0.6_2.12-${config.version}.jar")

  def ivy2File(implicit config: InstallerConfig): File = {
    new File(Properties.userHome, s".ivy2/local/$organization/${name}_sjs0.6_2.12/${config.version}/jars/${name}_sjs0.6_2.12.jar")
  }

  def publishLocal()(implicit config: InstallerConfig): Int = {
    logger.info(s"Publishing local '$name'...")
    report(Process(command = "sbt publishLocal", cwd = repoDir).!, action = "PublishLocal")
  }

  def unmatchedProject: Boolean = {
    def closer[A](bs: BufferedSource)(f: BufferedSource => A): A = try f(bs) finally bs.close()

    buildFile.exists() && !closer(Source.fromFile(buildFile))(_.mkString.contains(s"""name := "$name""""))
  }

  def update(): Int = {
    logger.info(s"Updating '$name'...")
    report(s"git -C ./repos/$name pull".!, action = "Update")
  }

  private def report(exitCode: Int, action: String): Int = {
    if (exitCode != 0)
      logger.warn(s"$name: $action ended with exit code $exitCode")
    exitCode
  }

}
