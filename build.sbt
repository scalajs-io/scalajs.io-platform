import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys.{libraryDependencies, _}
import sbt.Project.projectToRef
import sbt._

import scala.language.postfixOps

val apiVersion = "0.4.0-pre2"
val angularVersion = apiVersion
val scalaJsVersion = "2.12.1"
//val scalaJsVersion = "2.11.8"

organization := "io.scalajs"

homepage := Some(url("https://github.com/scalajs-io/scalajs.io"))

val commonSettings = Seq(
  version := apiVersion,
  scalaVersion := scalaJsVersion,
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-Xlint"),
  scalacOptions in(Compile, doc) ++= Seq("-no-link-warnings"),
  autoCompilerPlugins := true,
  scalaJSModuleKind := ModuleKind.CommonJSModule,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaJsVersion,
    "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
  )) ++ publishingSettings

/////////////////////////////////////////////////////////////////////////////////
//      ScalaJs.io Core projects
/////////////////////////////////////////////////////////////////////////////////

lazy val core = (project in file("core")).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "core",
    organization := "io.scalajs",
    description := "ScalaJs.io Core classes and utilities",
    version := apiVersion
  )

lazy val dom_html = (project in file("dom-html")).
  dependsOn(core).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "dom-html",
    organization := "io.scalajs",
    description := "DOM/HTML bindings for Scala.js",
    version := apiVersion
  )

lazy val nodejs = (project in file("nodejs")).
  dependsOn(core).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "nodejs",
    organization := "io.scalajs",
    description := "NodeJS bindings for Scala.js",
    version := apiVersion
  )

lazy val build_tools = (project in file("build_tools")).
  dependsOn(nodejs, glob).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "build-verification",
    organization := "io.scalajs.tools",
    description := "Scalajs.io build verification tool",
    version := apiVersion
  )

/////////////////////////////////////////////////////////////////////////////////
//      Browser Platform projects
/////////////////////////////////////////////////////////////////////////////////

lazy val jquery = (project in file("jquery")).
  dependsOn(core, dom_html).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "jquery",
    organization := "io.scalajs",
    description := "JQuery bindings for Scala.js",
    version := apiVersion
  )

lazy val phaser = (project in file("phaser")).
  dependsOn(core, dom_html, pixijs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "phaser",
    organization := "io.scalajs",
    description := "Phaser 2.6.x bindings for Scala.js",
    version := apiVersion
  )

lazy val pixijs = (project in file("pixijs")).
  dependsOn(core, dom_html).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "pixijs",
    organization := "io.scalajs",
    description := "PIXI.js bindings for Scala.js",
    version := apiVersion
  )

/////////////////////////////////////////////////////////////////////////////////
//      Browser Platform / AngularJS projects
/////////////////////////////////////////////////////////////////////////////////

lazy val angular = (project in file("angular")).
  dependsOn(core, dom_html, jquery).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular",
    organization := "io.scalajs.npm",
    description := "AngularJS/core binding for Scala.js",
    version := angularVersion
  )

lazy val angular_anchorScroll = (project in file("angular-anchor-scroll")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-anchor-scroll",
    organization := "io.scalajs.npm",
    description := "AngularJS/anchorScroll binding for Scala.js",
    version := apiVersion
  )

lazy val angular_animate = (project in file("angular-animate")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-animate",
    organization := "io.scalajs.npm",
    description := "AngularJS/animate binding for Scala.js",
    version := angularVersion
  )

lazy val angular_cookies = (project in file("angular-cookies")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-cookies",
    organization := "io.scalajs.npm",
    description := "AngularJS/cookies binding for Scala.js",
    version := angularVersion
  )

lazy val angular_facebook = (project in file("angular-facebook")).
  dependsOn(angular, facebook).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-facebook",
    organization := "io.scalajs.npm",
    description := "AngularJS/facebook binding for Scala.js",
    version := apiVersion
  )

lazy val angular_md5 = (project in file("angular-md5")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-md5",
    organization := "io.scalajs.npm",
    description := "AngularJS/md5 binding for Scala.js",
    version := apiVersion
  )

lazy val angular_file_upload = (project in file("angular-file-upload")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-file-upload",
    organization := "io.scalajs.npm",
    description := "AngularJS/fileupload binding for Scala.js",
    version := apiVersion
  )

lazy val angular_nvd3 = (project in file("angular-nvd3")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-nvd3",
    organization := "io.scalajs.npm",
    description := "AngularJS/nvd3 binding for Scala.js",
    version := apiVersion
  )

lazy val angular_sanitize = (project in file("angular-sanitize")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-sanitize",
    organization := "io.scalajs.npm",
    description := "AngularJS/sanitize binding for Scala.js",
    version := angularVersion
  )

lazy val angular_ui_bootstrap = (project in file("angular-ui-bootstrap")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-ui-bootstrap",
    organization := "io.scalajs.npm",
    description := "AngularJS/ui-bootstrap binding for Scala.js",
    version := angularVersion
  )

lazy val angular_ui_router = (project in file("angular-ui-router")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angular-ui-router",
    organization := "io.scalajs.npm",
    description := "AngularJS/ui-router binding for Scala.js",
    version := angularVersion
  )

lazy val angularjs_toaster = (project in file("angularjs-toaster")).
  dependsOn(angular).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "angularjs-toaster",
    organization := "io.scalajs.npm",
    description := "AngularJS/toaster binding for Scala.js",
    version := apiVersion
  )

lazy val angular_platform = (project in file("bundles/angularjs")).
  aggregate(
    angular, angular_anchorScroll, angular_animate, angular_cookies, angular_facebook, angular_file_upload,
    angular_md5, angular_nvd3, angular_sanitize, angularjs_toaster, angular_ui_bootstrap, angular_ui_router).
  dependsOn(
    angular, angular_anchorScroll, angular_animate, angular_cookies, angular_facebook, angular_file_upload,
    angular_md5, angular_nvd3, angular_sanitize, angularjs_toaster, angular_ui_bootstrap, angular_ui_router).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "angular-bundle",
    organization := "io.scalajs.npm",
    description := "AngularJS platform bundle",
    version := angularVersion
  )

/////////////////////////////////////////////////////////////////////////////////
//      NPM packages
/////////////////////////////////////////////////////////////////////////////////

lazy val async = (project in file("async")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "async",
    organization := "io.scalajs.npm",
    description := "async binding for Scala.js",
    version := apiVersion
  )

lazy val bcrypt = (project in file("bcrypt")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "bcrypt",
    organization := "io.scalajs.npm",
    description := "bcrypt binding for Scala.js",
    version := apiVersion
  )

lazy val bignum = (project in file("bignum")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "bignum",
    organization := "io.scalajs.npm",
    description := "bignum binding for Scala.js",
    version := apiVersion
  )

lazy val body_parser = (project in file("body-parser")).
  dependsOn(nodejs, express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "body-parser",
    organization := "io.scalajs.npm",
    description := "body-parser binding for Scala.js",
    version := apiVersion
  )

lazy val brake = (project in file("brake")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "brake",
    organization := "io.scalajs.npm",
    description := "brake binding for Scala.js",
    version := apiVersion
  )

lazy val buffermaker = (project in file("buffermaker")).
  dependsOn(nodejs, bignum).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "buffermaker",
    organization := "io.scalajs.npm",
    description := "buffermaker binding for Scala.js",
    version := apiVersion
  )

lazy val cassandra_driver = (project in file("cassandra-driver")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "cassandra-driver",
    organization := "io.scalajs.npm",
    description := "cassandra-driver binding for Scala.js",
    version := apiVersion
  )

lazy val chalk = (project in file("chalk")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "chalk",
    organization := "io.scalajs.npm",
    description := "chalk binding for Scala.js",
    version := apiVersion
  )

lazy val cheerio = (project in file("cheerio")).
  dependsOn(dom_html, jquery, nodejs, htmlparser2).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "cheerio",
    organization := "io.scalajs.npm",
    description := "cheerio binding for Scala.js",
    version := apiVersion
  )

lazy val colors = (project in file("colors")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "colors",
    organization := "io.scalajs.npm",
    description := "colors binding for Scala.js",
    version := apiVersion
  )

lazy val cookie = (project in file("cookie")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "cookie",
    organization := "io.scalajs.npm",
    description := "cookie binding for Scala.js",
    version := apiVersion
  )

lazy val cookie_parser = (project in file("cookie-parser")).
  dependsOn(nodejs, cookie, express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "cookie-parser",
    organization := "io.scalajs.npm",
    description := "cookie-parser binding for Scala.js",
    version := apiVersion
  )

lazy val csv_parse = (project in file("csv-parse")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _ *).
  settings(
    name := "csv-parse",
    organization := "io.scalajs.npm",
    description := "csv-parse binding for Scala.js",
    version := apiVersion
  )

lazy val csvtojson = (project in file("csvtojson")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "csvtojson",
    organization := "io.scalajs.npm",
    description := "csvtojson binding for Scala.js",
    version := apiVersion
  )

lazy val drama = (project in file("drama")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "drama",
    organization := "io.scalajs.npm",
    description := "drama binding for Scala.js",
    version := apiVersion
  )

lazy val escape_html = (project in file("escape-html")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "escape-html",
    organization := "io.scalajs.npm",
    description := "escape-html binding for Scala.js",
    version := apiVersion
  )

lazy val express = (project in file("express")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "express",
    organization := "io.scalajs.npm",
    description := "express binding for Scala.js",
    version := apiVersion
  )

lazy val express_csv = (project in file("express-csv")).
  dependsOn(express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "express-csv",
    organization := "io.scalajs.npm",
    description := "expressjs-csv binding for Scala.js",
    version := apiVersion
  )

lazy val express_fileupload = (project in file("express-fileupload")).
  dependsOn(express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "express-fileupload",
    organization := "io.scalajs.npm",
    description := "expressjs-fileupload binding for Scala.js",
    version := apiVersion
  )

lazy val express_ws = (project in file("express-ws")).
  dependsOn(express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "express-ws",
    organization := "io.scalajs.npm",
    description := "expressjs-ws binding for Scala.js",
    version := apiVersion
  )

lazy val feedparser = (project in file("feedparser-promised")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "feedparser-promised",
    organization := "io.scalajs.npm",
    description := "feedparser-promised binding for Scala.js",
    version := apiVersion
  )

lazy val filed = (project in file("filed")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "filed",
    organization := "io.scalajs.npm",
    description := "filed binding for Scala.js",
    version := apiVersion
  )

lazy val github_api_node = (project in file("github-api-node")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "github-api-node",
    organization := "io.scalajs.npm",
    description := "A higher-level wrapper around the Github API.",
    version := apiVersion
  )

lazy val glob = (project in file("glob")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "glob",
    organization := "io.scalajs.npm",
    description := "glob binding for Scala.js",
    version := apiVersion
  )

lazy val gzip_uncompressed_size = (project in file("gzip-uncompressed-size")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "gzip-uncompressed-size",
    organization := "io.scalajs.npm",
    description := "glob binding for Scala.js",
    version := apiVersion
  )

lazy val html_to_json = (project in file("html-to-json")).
  dependsOn(dom_html, jquery, nodejs, request).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "html-to-json",
    organization := "io.scalajs.npm",
    description := "html-to-json binding for Scala.js",
    version := apiVersion
  )

lazy val htmlparser2 = (project in file("htmlparser2")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "htmlparser2",
    organization := "io.scalajs.npm",
    description := "htmlparser2 binding for Scala.js",
    version := apiVersion
  )

lazy val ip = (project in file("ip")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "ip",
    organization := "io.scalajs.npm",
    description := "IP address utilities for node.js",
    version := apiVersion
  )

lazy val jsdom = (project in file("jsdom")).
  dependsOn(nodejs, dom_html, jquery).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "jsdom",
    organization := "io.scalajs.npm",
    description := "jsdom binding for Scala.js",
    version := apiVersion
  )

lazy val jwt_simple = (project in file("jwt-simple")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "jwt-simple",
    organization := "io.scalajs.npm",
    description := "jwt-simple binding for Scala.js",
    version := apiVersion
  )

lazy val kafka_node = (project in file("kafka-node")).
  dependsOn(nodejs, node_zookeeper_client).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "kafka-node",
    organization := "io.scalajs.npm",
    description := "kafka-node binding for Scala.js",
    version := apiVersion
  )

/*
lazy val kafka_rest = (project in file("kafka-rest")).
  dependsOn(nodejs, node_zookeeper_client).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "kafka-rest",
    organization := "io.scalajs.npm",
    description := "kafka-rest binding for Scala.js",
    version := apiVersion
  )*/

lazy val md5 = (project in file("md5")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "md5",
    organization := "io.scalajs.npm",
    description := "md5 binding for Scala.js",
    version := apiVersion
  )

lazy val memory_fs = (project in file("memory-fs")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "memory-fs",
    organization := "io.scalajs.npm",
    description := "memory-fs binding for Scala.js",
    version := apiVersion
  )

lazy val minimist = (project in file("minimist")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "minimist",
    organization := "io.scalajs.npm",
    description := "parse argument options.",
    version := apiVersion
  )

lazy val mkdirp = (project in file("mkdirp")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "mkdirp",
    organization := "io.scalajs.npm",
    description := "mkdirp binding for Scala.js",
    version := apiVersion
  )

lazy val moment = (project in file("moment")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "moment",
    organization := "io.scalajs.npm",
    description := "moment binding for Scala.js",
    version := apiVersion
  )

lazy val moment_duration_format = (project in file("moment-duration-format")).
  dependsOn(nodejs, moment).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "moment-duration-format",
    organization := "io.scalajs.npm",
    description := "A moment.js plugin for formatting durations.",
    version := apiVersion
  )

lazy val moment_range = (project in file("moment-range")).
  dependsOn(nodejs, moment).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "moment-range",
    organization := "io.scalajs.npm",
    description := "Fancy date ranges for Moment.js.",
    version := apiVersion
  )

lazy val moment_timezone = (project in file("moment-timezone")).
  dependsOn(nodejs, moment).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "moment-timezone",
    organization := "io.scalajs.npm",
    description := "moment-timezone binding for Scala.js",
    version := apiVersion
  )

lazy val mongodb = (project in file("mongodb")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "mongodb",
    organization := "io.scalajs.npm",
    description := "The official MongoDB driver for Node.js.",
    version := apiVersion
  )

lazy val mongoose = (project in file("mongoose")).
  dependsOn(nodejs, mongodb, mpromise).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "mongoose",
    organization := "io.scalajs.npm",
    description := "Mongoose MongoDB ODM [Scala.js]",
    version := apiVersion
  )

lazy val mpromise = (project in file("mpromise")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "mpromise",
    organization := "io.scalajs.npm",
    description := "A promises/A+ conformant implementation, written for mongoose [Scala.js]",
    version := apiVersion
  )

lazy val multer = (project in file("multer")).
  dependsOn(express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "multer",
    organization := "io.scalajs.npm",
    description := "expressjs-multer binding for Scala.js",
    version := apiVersion
  )

lazy val mysql = (project in file("mysql")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "mysql",
    organization := "io.scalajs.npm",
    description := "mysql binding for Scala.js",
    version := apiVersion
  )

lazy val node_zookeeper_client = (project in file("node-zookeeper-client")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "node-zookeeper-client",
    organization := "io.scalajs.npm",
    description := "node-zookeeper-client binding for Scala.js",
    version := apiVersion
  )

lazy val numeral = (project in file("numeral")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "numeral",
    organization := "io.scalajs.npm",
    description := "numeral binding for Scala.js",
    version := apiVersion
  )

lazy val oppressor = (project in file("oppressor")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "oppressor",
    organization := "io.scalajs.npm",
    description := "oppressor binding for Scala.js",
    version := apiVersion
  )

lazy val readable_stream = (project in file("readable-stream")).
  dependsOn(nodejs, tough_cookie).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "readable-stream",
    organization := "io.scalajs.npm",
    description := "readable-stream binding for Scala.js",
    version := apiVersion
  )

lazy val request = (project in file("request")).
  dependsOn(nodejs, tough_cookie).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "request",
    organization := "io.scalajs.npm",
    description := "request binding for Scala.js",
    version := apiVersion
  )

lazy val rxjs = (project in file("rx")).
  aggregate(transducers).
  dependsOn(nodejs, transducers).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "rx",
    organization := "io.scalajs.npm",
    description := "rx.js binding for Scala.js",
    version := apiVersion
  )

/*
lazy val should = (project in file("should")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "should",
    organization := "io.scalajs.npm",
    description := "should binding for Scala.js",
    version := apiVersion
  )*/

/*
lazy val socketio_client = (project in file("socket.io/client")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "socket.io-client",
    organization := "io.scalajs.npm",
    description := "socket.io-client binding for Scala.js",
    version := apiVersion
  )

lazy val socketio_server = (project in file("socket.io/server")).
  dependsOn(nodejs, socketio_client, express).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "socket.io-server",
    organization := "io.scalajs.npm",
    description := "socket.io binding for Scala.js",
    version := apiVersion
  )*/

lazy val splitargs = (project in file("splitargs")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "splitargs",
    organization := "io.scalajs.npm",
    description := "splitargs binding for Scala.js",
    version := apiVersion
  )

lazy val stream_throttle = (project in file("stream-throttle")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "stream-throttle",
    organization := "io.scalajs.npm",
    description := "stream-throttle binding for Scala.js",
    version := apiVersion
  )

lazy val throttle = (project in file("throttle")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "throttle",
    organization := "io.scalajs.npm",
    description := "Node.js Transform stream that passes data through at n bytes per second",
    version := apiVersion
  )

lazy val tingodb = (project in file("tingodb")).
  dependsOn(nodejs, mongodb).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "tingodb",
    organization := "io.scalajs.npm",
    description := "tingodb binding for Scala.js",
    version := apiVersion
  )

lazy val tough_cookie = (project in file("tough-cookie")).
  dependsOn(nodejs, mongodb).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "tough-cookie",
    organization := "io.scalajs.npm",
    description := "tough-cookie binding for Scala.js",
    version := apiVersion
  )

lazy val transducers = (project in file("transducers-js")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "transducers-js",
    organization := "io.scalajs.npm",
    description := "transducers-js binding for Scala.js",
    version := apiVersion
  )

lazy val type_is = (project in file("type-is")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "type-is",
    organization := "io.scalajs.npm",
    description := "type-is binding for Scala.js",
    version := apiVersion
  )

lazy val watch = (project in file("watch")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "watch",
    organization := "io.scalajs.npm",
    description := "watch binding for Scala.js",
    version := apiVersion
  )

lazy val winston = (project in file("winston")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "winston",
    organization := "io.scalajs.npm",
    description := "winston binding for Scala.js",
    version := apiVersion
  )

lazy val winston_daily_rotate_file = (project in file("winston-daily-rotate-file")).
  dependsOn(nodejs, winston).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "winston-daily-rotate-file",
    organization := "io.scalajs.npm",
    description := "winston-daily-rotate-file binding for Scala.js",
    version := apiVersion
  )

lazy val xml2js = (project in file("xml2js")).
  dependsOn(nodejs).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "xml2js",
    organization := "io.scalajs.npm",
    description := "xml2js binding for Scala.js",
    version := apiVersion
  )

/////////////////////////////////////////////////////////////////////////////////
//      Social sub-projects
/////////////////////////////////////////////////////////////////////////////////

lazy val facebook = (project in file("facebook-api")).
  dependsOn(core).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "facebook-api",
    organization := "io.scalajs",
    description := "Social/Facebook bindings for Scala.js",
    version := apiVersion
  )

lazy val linkedin = (project in file("linkedin-api")).
  dependsOn(core).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(
    name := "linkedin-api",
    organization := "io.scalajs",
    description := "Social/LinkedIn bindings for Scala.js",
    version := apiVersion
  )

/////////////////////////////////////////////////////////////////////////////////
//      Bundles
/////////////////////////////////////////////////////////////////////////////////

lazy val complete_platform = (project in file("bundles/complete")).
  aggregate(
    core, dom_html, jquery, nodejs, phaser, pixijs, facebook, linkedin, angular_platform, mean_stack,
    // npm packages
    async, bcrypt, bignum, body_parser, brake, buffermaker, cassandra_driver, chalk, cheerio,
    colors, cookie, cookie_parser, csv_parse, csvtojson, drama, escape_html,
    express, express_csv, express_fileupload, express_ws, feedparser, filed, github_api_node,
    glob, gzip_uncompressed_size, html_to_json, htmlparser2, ip, jsdom, jwt_simple, kafka_node, /*kafka_rest,*/
    md5, memory_fs, mkdirp, moment, moment_duration_format, moment_range, moment_timezone,
    mongodb, mongoose, mpromise, multer, mysql, node_zookeeper_client,
    numeral, oppressor, readable_stream, request, rxjs, /*should, socketio_client, socketio_server,*/ splitargs,
    stream_throttle, throttle, tingodb, tough_cookie, transducers, type_is,
    watch, winston, winston_daily_rotate_file, xml2js).
  dependsOn(
    core, dom_html, jquery, nodejs, phaser, pixijs, facebook, linkedin, angular_platform, mean_stack,
    // npm packages
    async, bcrypt, bignum, body_parser, brake, buffermaker, cassandra_driver, chalk, cheerio,
    colors, cookie, cookie_parser, csv_parse, csvtojson, drama, escape_html,
    express, express_csv, express_fileupload, express_ws, feedparser, filed, github_api_node,
    glob, gzip_uncompressed_size, html_to_json, htmlparser2, ip, jsdom, jwt_simple, kafka_node, /*kafka_rest,*/
    md5, memory_fs, mkdirp, moment, moment_duration_format, moment_range, moment_timezone,
    mongodb, mongoose, mpromise, multer, mysql, node_zookeeper_client,
    numeral, oppressor, readable_stream, request, rxjs, /*should, socketio_client, socketio_server,*/ splitargs,
    stream_throttle, throttle, tingodb, tough_cookie, transducers, type_is,
    watch, winston, winston_daily_rotate_file, xml2js).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "complete-platform",
    organization := "io.scalajs",
    description := "Complete platform bundle"
  )

lazy val mean_stack = (project in file("bundles/mean_stack")).
  aggregate(nodejs, body_parser, express, mongodb).
  dependsOn(nodejs, body_parser, express, mongodb).
  enablePlugins(ScalaJSPlugin).
  settings(commonSettings: _*).
  settings(publishingSettings: _*).
  settings(
    name := "mean-stack",
    organization := "io.scalajs.npm",
    description := "MEAN Stack bundle"
  )

/////////////////////////////////////////////////////////////////////////////////
//      Publishing
/////////////////////////////////////////////////////////////////////////////////

lazy val publishingSettings = Seq(
  sonatypeProfileName := "org.xerial",
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra :=
    <url>https://github.com/scalajs-io/scalajs.io</url>
      <licenses>
        <license>
          <name>MIT License</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:github.com/scalajs-io/scalajs.io.git</connection>
        <developerConnection>scm:git:git@github.com:scalajs-io/scalajs.io.git</developerConnection>
        <url>github.com/scalajs-io/scalajs.io.git</url>
      </scm>
      <developers>
        <developer>
          <id>ldaniels528</id>
          <name>Lawrence Daniels</name>
          <email>lawrence.daniels@gmail.com</email>
          <organization>io.scalajs</organization>
          <organizationUrl>https://github.com/scalajs-io</organizationUrl>
          <roles>
            <role>Project-Administrator</role>
            <role>Developer</role>
          </roles>
          <timezone>+7</timezone>
        </developer>
      </developers>
)

// loads the Scalajs-io root project at sbt startup
onLoad in Global := (Command.process("project complete_platform", _: State)) compose (onLoad in Global).value
