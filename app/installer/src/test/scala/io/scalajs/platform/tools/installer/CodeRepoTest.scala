package io.scalajs.platform.tools.installer

import java.io.File

import org.scalatest.FunSpec

import scala.util.Properties

/**
  * Code Repository Test Suite
  * @author lawrence.daniels@gmail.com
  */
class CodeRepoTest extends FunSpec {
  private implicit val config: InstallerConfig = InstallerConfig("/installer-config.json")
  private val repo = config.repositories.find(_.name == "core")
    .getOrElse(throw new IllegalStateException("Project 'core' was not found"))

  describe(classOf[CodeRepo].getSimpleName) {

    it("identify the path of the compiled jar") {
      info(s"jarFile: ${repo.jarFile}")
      assert(repo.jarFile == new File(repo.rootDirectory, s"target/scala-2.12/${repo.name}_sjs0.6_2.12-${config.version}.jar"))
    }

    it("identify the path of the locally published jar") {
      info(s"ivy2File: ${repo.ivy2File}")
      assert(repo.ivy2File == new File(s"${Properties.userHome}/.ivy2/local/io.scalajs/core_sjs0.6_2.12/${config.version}/jars/core_sjs0.6_2.12.jar"))
    }

  }

}
