package io.scalajs.platform.tools.installer

import akka.actor.{Actor, ActorLogging}
import io.scalajs.platform.tools.installer.InstallationActor._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
  * Installation Actor
  * @param config the implicit [[InstallerConfig]]
  * @param ctx    the implicit [[ProcessingContext]]
  */
class InstallationActor()(implicit config: InstallerConfig, ctx: ProcessingContext) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(getClass)

  override def receive: Receive = {
    case command@Compile(repo, attempts) =>
      if (repo.jarFile.exists()) ctx.completed(repo)
      else if (!repo.isSatisfied) reschedule(repo, command.copy(attempts = attempts + 1))
      else if (repo.compile() == 0) ctx.completed(repo)
      else ctx.fail(s"${repo.name} failed to compile")

    case command@Download(repo, attempts) =>
      if (repo.jarFile.exists()) ctx.completed(repo)
      else if (repo.downloadOrUpdate() == 0) ctx.completed(repo)
      else reschedule(repo, command.copy(attempts = attempts + 1))

    case command@PublishLocal(repo, attempts) =>
      if (repo.ivy2File.exists()) ctx.completed(repo)
      else if (!repo.isSatisfied) reschedule(repo, command.copy(attempts = attempts + 1))
      else if (repo.publishLocal() == 0) ctx.completed(repo)
      else ctx.fail(s"${repo.name} failed to publish locally")

    case message => unhandled(message)
  }

  def reschedule(repo: CodeRepo, command: InstallationCommand): Unit = {
    logger.info(s"${repo.name}: Rescheduled; missing dependencies = [${repo.dependencies.filterNot(ctx.isCompleted).mkString(", ")}]")
    import context.dispatcher
    context.system.scheduler.scheduleOnce(delay = 30.seconds) {
      self ! command
    }
  }

}

/**
  * Installation Actor Companion
  * @author lawrence.daniels@gmail.com
  */
object InstallationActor {

  sealed trait InstallationCommand {

    def repo: CodeRepo

    def attempts: Int

  }

  case class Compile(repo: CodeRepo, attempts: Int = 1) extends InstallationCommand

  case class Download(repo: CodeRepo, attempts: Int = 1) extends InstallationCommand

  case class PublishLocal(repo: CodeRepo, attempts: Int = 1) extends InstallationCommand

}
