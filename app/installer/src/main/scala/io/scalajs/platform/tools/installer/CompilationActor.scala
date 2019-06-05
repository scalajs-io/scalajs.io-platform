package io.scalajs.platform.tools.installer

import akka.actor.{Actor, ActorLogging}
import io.scalajs.platform.tools.installer.CompilationActor._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
  * Compilation Actor
  * @param config the implicit [[InstallerConfig]]
  * @param ctx    the implicit [[ProcessingContext]]
  */
class CompilationActor()(implicit config: InstallerConfig, ctx: ProcessingContext) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(getClass)

  override def receive: Receive = {
    case command@Compile(repo, attempts) =>
      if (repo.jarFile.exists()) ctx.completed(repo)
      else {
        if (!repo.isSatisfied) reschedule(repo, command.copy(attempts = attempts + 1))
        else if (!repo.jarFile.exists()) {
          if (repo.compile() == 0) ctx.completed(repo)
          else reschedule(repo, command.copy(attempts = attempts + 1))
        }
        else logger.info(s"${repo.name}: ${repo.jarFile.getName} has already been built")
      }

    case command@Download(repo, attempts) =>
      if (repo.jarFile.exists()) ctx.completed(repo)
      else {
        if (repo.downloadOrUpdate() == 0) ctx.completed(repo)
        else reschedule(repo, command.copy(attempts = attempts + 1))
      }

    case command@PublishLocal(repo, attempts) =>
      if (repo.ivy2File.exists()) ctx.completed(repo)
      else {
        if (!repo.isSatisfied) self ! command.copy(attempts = attempts + 1)
        else if (!repo.jarFile.exists()) {
          repo.publishLocal()
          ctx.completed(repo)
        }
        else reschedule(repo, command.copy(attempts = attempts + 1))
      }

    case message => unhandled(message)
  }

  def reschedule(repo: Repo, command: AnyRef): Unit = {
    logger.info(s"${repo.name}: Rescheduled; missing dependencies = [${repo.dependencies.filterNot(ctx.isCompleted).mkString(", ")}]")
    import context.dispatcher
    context.system.scheduler.scheduleOnce(delay = 30.seconds) {
      self ! command
    }
  }

}

/**
  * Compilation Actor Companion
  * @author lawrence.daniels@gmail.com
  */
object CompilationActor {

  case class Compile(repo: Repo, attempts: Int = 1)

  case class Download(repo: Repo, attempts: Int = 1)

  case class PublishLocal(repo: Repo, attempts: Int = 1)

}
