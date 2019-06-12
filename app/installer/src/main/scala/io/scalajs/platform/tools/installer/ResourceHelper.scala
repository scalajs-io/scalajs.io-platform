package io.scalajs.platform.tools.installer

import scala.language.reflectiveCalls

/**
  * Resource Helper
  * @author lawrence.daniels@coxautoinc.com
  */
object ResourceHelper {

  type Manageable = {
    def open(): Unit

    def close(): Unit
  }

  /**
    * Automatically closes a resource after the completion of a code block
    */
  final implicit class AutoOpenClose[T <: Manageable](val resource: T) extends AnyVal {

    def manage[S](block: T => S): S = try {
      resource.open()
      block(resource)
    } finally resource.close()

  }

  /**
    * Automatically closes a resource after the completion of a code block
    */
  final implicit class AutoClose[T <: {def close()}](val resource: T) extends AnyVal {

    def use[S](block: T => S): S = try block(resource) finally resource.close()

  }

  /**
    * Automatically closes a resource after the completion of a code block
    */
  final implicit class AutoDisconnect[T <: {def disconnect()}](val resource: T) extends AnyVal {

    def use[S](block: T => S): S = try block(resource) finally resource.disconnect()

  }

  /**
    * Automatically closes a resource after the completion of a code block
    */
  final implicit class AutoShutdown[T <: {def shutdown()}](val resource: T) extends AnyVal {

    def use[S](block: T => S): S = try block(resource) finally resource.shutdown()

  }

}

