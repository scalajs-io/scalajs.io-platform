package io.scalajs.platform.tools.installer

import org.scalatest.FunSpec

import scala.util.Properties

/**
  * Repository Test Suite
  * @author lawrence.daniels@gmail.com
  */
class RepoTest extends FunSpec {
  private implicit val config: InstallerConfig = InstallerConfig("/installer-config.json")

  describe(classOf[Repo].getSimpleName) {

    it("identify the path of the locally published jar") {
      val repo = config.repos.find(_.name == "core")
        .getOrElse(throw new IllegalStateException("Project 'core' was not found"))

      info(s"ivy2File: ${repo.ivy2File}")
      assert(repo.ivy2File.getCanonicalPath == s"${Properties.userHome}/.ivy2/local/io.scalajs/core_sjs0.6_2.12/${config.version}/jars/core_sjs0.6_2.12.jar")
    }

  }

}
