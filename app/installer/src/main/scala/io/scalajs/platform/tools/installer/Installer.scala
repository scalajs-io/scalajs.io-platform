package io.scalajs.platform.tools.installer

import java.io.File

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import io.scalajs.platform.tools.installer.InstallationActor._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Try

/**
  * Node.js Platform Download and Installer
  * @author lawrence.daniels@gmail.com
  */
object Installer {
  private[this] val logger = LoggerFactory.getLogger(getClass)

  /**
    * Application invocation
    * @param args the command line arguments
    */
  def main(args: Array[String]): Unit = {
    // load the configuration
    implicit val config: InstallerConfig = InstallerConfig("/installer-config.json")
    implicit val ctx: ProcessingContext = ProcessingContext(config.repositories.length)

    // create the actor pool
    implicit val actorSystem: ActorSystem = ActorSystem(name = "InstallerSystem")
    implicit val ec: ExecutionContext = actorSystem.dispatcher
    implicit val actorPool: ActorRef = actorSystem.actorOf(Props(new InstallationActor())
      .withRouter(RoundRobinPool(3)), name = "InstallationActor")

    // define the valid set of actions
    val validActions = Map[String, Seq[CodeRepo] => Unit](
      "audit" -> audit,
      "commit" -> commit,
      "compile" -> compile,
      "npmCheckUpdates" -> npmCheckUpdates,
      "npmInstall" -> npmInstall,
      "publish" -> { repos: Seq[CodeRepo] => publishLocal(repos, force = false) },
      "publishLocal" -> { repos: Seq[CodeRepo] => publishLocal(repos, force = false) },
      "push" -> push,
      "refresh" -> commit,
      "repair" -> repair,
      "reset" -> reset,
      "status" -> status,
      "test" -> test,
      "update" -> downloadOrUpdateRepos
    )

    // start the process
    val repositories = config.repositories
    try {
      args foreach { action =>
        logger.info(s"Running action '$action'...")
        validActions.get(action) match {
          case Some(task) => task(repositories)
          case None =>
            throw new IllegalArgumentException(s"Invalid action '$action'. Valid actions are: ${validActions.keys.mkString(", ")}")
        }
      }
      logger.info("Done")
    } catch {
      case e: Exception =>
        logger.error("Fatal error", e)
    } finally actorSystem.terminate()

    System.exit(0)
  }

  def audit(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = repos.foreach { repo =>
    // check for missing files
    Seq(
      repo.buildFile, repo.buildPropertiesFile, repo.jarFile, repo.ivy2File, repo.nodeModulesDirectory,
      repo.packageJonFile, repo.pluginsFile, repo.readMeFile
    ).foreach { file => if (!file.exists()) logger.warn(s"${repo.name}: ${file.getName} is missing") }

    // look for miscellaneous issues
    if (repo.unmatchedProject) logger.warn(s"${repo.name}'s build script is invalid")

    // report on modified files
    val modifiedFiles = repo.listModifiedFiles
    if (modifiedFiles.nonEmpty) {
      logger.warn(s"${repo.name} ${modifiedFiles.length} file(s) modified")
    }
  }

  /**
    * Commits the given repositories to GitHub
    * @param repos the given collection of [[CodeRepo repositories]]
    */
  def commit(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = repos.foreach { repo =>
    Try(repo.gitAdd("package.json")) // TODO temporary
    Try(repo.gitAdd("package-lock.json")) // TODO temporary
    val modifiedFiles = repo.listModifiedFiles
    if (modifiedFiles.nonEmpty) {
      logger.info(s"${repo.name}: ${modifiedFiles.length} file(s) modified")
      repo.commit(s"Release v${config.version}", modifiedFiles)
    }
  }

  def compile(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = repos.foreach { repo =>
    if (!repo.jarFile.exists()) {
      logger.info(s"Compiling '${repo.name}'...")
      repo.compile()
    }
  }

  def npmCheckUpdates(repos: Seq[CodeRepo]): Unit = repos foreach { repo =>
    logger.info(s"Checking for NPM dependency updates '${repo.name}'...")
    repo.npmCheckUpdates()
  }

  def npmInstall(repos: Seq[CodeRepo]): Unit = repos foreach { repo =>
    logger.info(s"Installing NPM modules in '${repo.name}'...")
    repo.npmInstall()
  }

  /**
    * Starts the download/publishing process
    * @param repos     the collection of [[CodeRepo repositories]]
    * @param force     indicates whether publishing should be forced
    * @param timeOut   the maximum allowed [[FiniteDuration duration]] for processing
    * @param ec        the implicit [[ExecutionContext]]
    * @param config    the implicit [[InstallerConfig]]
    * @param ctx       the implicit [[ProcessingContext]]
    * @param actorPool the implicit [[ActorRef]]
    */
  def publishLocal(repos: Seq[CodeRepo], force: Boolean, timeOut: FiniteDuration = 2.hours)
                  (implicit ec: ExecutionContext, config: InstallerConfig, ctx: ProcessingContext, actorPool: ActorRef): Unit = {
    // create the base directories
    createBaseDirectories(repos)

    // download (clone) or update (pull) each repo
    downloadOrUpdateRepos(repos)

    // perform an audit of unpublished repositories
    val unpublishedRepos = repos.filterNot(_.ivy2File.exists())
    logger.info(s"There are currently ${unpublishedRepos.length} unpublished repositories")

    // determine which have been completed already
    repos.foreach(repo => if (repo.ivy2File.exists()) ctx.completed(repo))
    logger.info(s"Processing ${ctx.remainingCount} of ${ctx.repoCount} repositories...")

    // schedule each repo for processing
    unpublishedRepos foreach {
      case repo if repo.ivy2File.exists() => ctx.completed(repo)
      case repo => actorPool ! PublishLocal(repo)
    }

    // display the results
    val outcome = ctx.completionPromise map { elapsedTime =>
      logger.info(s"Processed ${ctx.completedCount} of ${repos.size} repositories in ${elapsedTime / 1000} seconds")
      repos.groupBy(_.organization) foreach { case (organization, orgRepos) =>
        logger.info(s"$organization: ${orgRepos.length} repositories")
      }
    }

    // wait for the process to complete
    Await.result(outcome, timeOut)
  }

  def push(repos: Seq[CodeRepo]): Unit = repos.foreach { repo =>
    logger.info(s"${repo.name} pushing to origin...")
    repo.push()
  }

  def repair(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = repos.foreach { repo =>
    logger.info(s"${repo.name} repairing...")
    repo.npmCheckUpdates()
    repo.npmInstall()
    ensureProjectFiles(repo)
  }

  def reset(repos: Seq[CodeRepo]): Unit = repos.foreach { repo =>
    logger.info(s"${repo.name} resetting...")
    repo.reset()
  }

  def status(repos: Seq[CodeRepo]): Unit = {
    val modifiedRepos = repos.map(repo => repo -> repo.listModifiedFiles).filter { case (_, modifiedFiles) => modifiedFiles.nonEmpty }
    if (modifiedRepos.isEmpty) logger.info("All repositories are up-to-date")
    else {
      modifiedRepos.foreach { case (repo, modifiedFiles) =>
        logger.info(s"${repo.name}: ${modifiedFiles.length} file(s) modified")
      }
    }
  }

  def test(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = repos.foreach { repo =>
    if (!repo.nodeModulesDirectory.exists()) {
      logger.info(s"${repo.name} npm install")
      repo.npmInstall()
    }

    logger.info(s"${repo.name} test")
    repo.test()
  }

  private def createBaseDirectories(repos: Seq[CodeRepo]): Unit = {
    logger.info("Creating base directories...")
    repos.groupBy(_.rootDirectory.getParentFile).foreach { case (directory, _) =>
      if (!directory.exists()) {
        logger.info(s"Creating directory '${directory.getCanonicalPath}'...")
        if (!directory.mkdirs())
          logger.error(s"Failed to create directory '${directory.getCanonicalPath}'")
      }
    }
  }

  private def downloadOrUpdateRepos(repos: Seq[CodeRepo])(implicit config: InstallerConfig): Unit = {
    repos.foreach { repo =>
      try repo.downloadOrUpdate() catch {
        case e: Exception =>
          logger.error(s"${repo.name}: Failed to download or update - ${e.getMessage}")
      }
      ensureProjectFiles(repo)
    }
  }

  private def ensureProjectFiles(repo: CodeRepo)(implicit config: InstallerConfig): Unit = {
    ensureProjectFile(repo, _.buildFile, FileGenerator.createBuildFile)
    ensureProjectFile(repo, _.buildPropertiesFile, FileGenerator.createBuildPropertiesFile)
    ensureProjectFile(repo, _.packageJonFile, FileGenerator.createPackageJsonFile)
    ensureProjectFile(repo, _.pluginsFile, FileGenerator.createPluginsFile)
    ensureProjectFile(repo, _.readMeFile, FileGenerator.createReadMeFile)
    if (!repo.nodeModulesDirectory.exists()) {
      repo.npmCheckUpdates()
      repo.npmInstall()
    }
  }

  private def ensureProjectFile(repo: CodeRepo, toFile: CodeRepo => File, generateFile: CodeRepo => File): Option[File] =
    if (!toFile(repo).exists()) Option(generateFile(repo)) else None

}
