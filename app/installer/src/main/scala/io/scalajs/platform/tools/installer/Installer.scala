package io.scalajs.platform.tools.installer

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool
import io.scalajs.platform.tools.installer.InstallationActor._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

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
      .withRouter(RoundRobinPool(3)), name = "CompilationActor")

    // start the process
    try start(config.repositories, force = false) finally actorSystem.terminate()
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
  def start(repos: Seq[CodeRepo], force: Boolean, timeOut: FiniteDuration = 2.hours)
           (implicit ec: ExecutionContext, config: InstallerConfig, ctx: ProcessingContext, actorPool: ActorRef): Unit = {
    // create the base directory
    repos.headOption.foreach(_.rootDirectory.getParentFile.mkdirs())

    // download (clone) or update (pull) each repo
    repos.foreach(_.downloadOrUpdate())

    // perform an audit of unpublished repositories
    val unpublishedRepos = repos.filterNot(_.ivy2File.exists())
    logger.info(s"There are currently ${unpublishedRepos.length} unpublished repositories")

    // determine which have been completed already
    repos.foreach(repo => if (repo.ivy2File.exists()) ctx.completed(repo))
    logger.info(s"Processing ${ctx.remainingCount} of ${ctx.repoCount} repositories...")

    // schedule each repo for processing
    unpublishedRepos foreach { repo =>
      if (repo.unmatchedProject) logger.warn(s"${repo.name}: Build script is invalid")
      else if (repo.ivy2File.exists()) ctx.completed(repo)
      else actorPool ! PublishLocal(repo)
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

}
