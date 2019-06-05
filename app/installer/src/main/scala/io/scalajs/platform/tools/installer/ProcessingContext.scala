package io.scalajs.platform.tools.installer

import scala.collection.concurrent.TrieMap
import scala.concurrent.{Future, Promise}

case class ProcessingContext(repoCount: Int) {
  private val mappings = TrieMap[String, Repo]()
  private val promise = Promise[Unit]()

  val completionPromise: Future[Unit] = promise.future

  def isCompleted(name: String): Boolean = mappings.contains(name)

  def isDone: Boolean = mappings.size == repoCount

  def completed(repo: Repo): Unit = {
    mappings(repo.name) = repo
    if (isDone) promise.success(())
  }

  def completedCount: Int = mappings.size

  def completedNames: Iterable[String] = mappings.keys

  def remainingCount: Int = repoCount - completedCount

}