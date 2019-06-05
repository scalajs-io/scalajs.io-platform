package io.scalajs.platform.tools.installer

import scala.collection.concurrent.TrieMap
import scala.concurrent.{Future, Promise}

/**
  * Represents the Processing Context
  * @param repoCount the total number of repositories
  */
case class ProcessingContext(repoCount: Int) {
  private val mappings = TrieMap[String, Repo]()
  private val promise = Promise[Long]()
  private val startTime = System.currentTimeMillis()

  val completionPromise: Future[Long] = promise.future

  def fail(message: String): Unit = promise.failure(new IllegalStateException(message))

  def isCompleted(name: String): Boolean = mappings.contains(name)

  def isCompleted(repo: Repo): Boolean = mappings.contains(repo.name)

  def isDone: Boolean = mappings.size == repoCount

  def completed(repo: Repo): Unit = {
    mappings(repo.name) = repo
    if (isDone) promise.success(System.currentTimeMillis() - startTime)
  }

  def completedCount: Int = mappings.size

  def completedNames: Iterable[String] = mappings.keys

  def remainingCount: Int = repoCount - completedCount

}