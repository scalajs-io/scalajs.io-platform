ScalaJs.io Complete Platform
============================

### Description

This project is used for locally building and publishing all components of the ScalaJs.io platform; including:

* [Core components](https://github.com/scalajs-io/scalajs.io) (Core, DOM/HTML, Node.js)
* All browser-based packages
* All npm packages

### Client-Side Components

#### Available AngularJS (1.5.x) Components

| Service           | Artifact ID                    | Description                      |
|-------------------|--------------------------------|----------------------------------|
| $anchorScroll     | angularjs-anchor-scroll        | Anchor Scroll Service. |
| $animate          | angularjs-animate              | The $animate service exposes a series of DOM utility methods that provide support for animation hooks. |
| $cacheFactory     | angularjs-core                 | Factory that constructs cache objects and gives access to them. |
| $compile          | angularjs-core                 | Compiles an HTML string or DOM into a template and produces a template function, which can then be used to link scope and the template together. |
| $cookies          | angularjs-cookies              | Provides read/write access to browser's cookies. |
| $cookieStore      | angularjs-cookies              | Provides a key-value (string-object) storage, that is backed by session cookies. |
| $exceptionHandler | angularjs-core                 | Any uncaught exception in angular expressions is delegated to this service. |
| $filter           | angularjs-core                 | Selects a subset of items from array and returns it as a new array. |
| $http             | angularjs-core                 | The $http service is a core Angular service that facilitates communication with the remote HTTP servers via the browser's XMLHttpRequest object or via JSONP. |
| $injector         | angularjs-core                 | $injector is used to retrieve object instances as defined by provider, instantiate types, invoke methods, and load modules. |
| $interval         | angularjs-core                 | Angular's wrapper for window.setInterval. The fn function is executed every delay milliseconds. |
| $location         | angularjs-core                 | The $location service parses the URL in the browser address bar (based on the window.location) and makes the URL available to your application. |
| $log              | angularjs-core                 | Simple service for logging. Default implementation safely writes the message into the browser's console (if present). |
| $modal            | angularjs-ui-bootstrap         | Modal Dialogs - Angular UI Bootstrap |
| $nvd3             | angularjs-nvd3                 | An AngularJS directive for NVD3 re-usable charting library (based on D3).|
| $parse            | angularjs-core                 | Converts Angular expression into a function. |
| $q                | angularjs-core                 | A service that helps you run functions asynchronously, and use their return values (or exceptions) when they are done processing. |
| $resource         | angularjs-core                 | A factory which creates a resource object that lets you interact with RESTful server-side data sources. |
| $route            | angularjs-ui-router            | $route is used for deep-linking URLs to controllers and views (HTML partials). It watches $location.url() and tries to map the path to an existing route definition. |
| $sce              | angularjs-sanitize             | $sce is a service that provides Strict Contextual Escaping services to AngularJS. |
| $timeout          | angularjs-core                 | Angular's wrapper for window.setTimeout. The fn function is wrapped into a try/catch block and delegates any exceptions to $exceptionHandler service. |
| FacebookService   | angularjs-facebook             | AngularJS - Facebook service |
| FileUploader      | angularjs-nergvh-fileupload    | AngularJS File Uploader |
| md5               | angularjs-md5                  | A md5 crypto component for Angular.js. |
| toaster           | angularjs-toaster              | AngularJS Toaster is a customized version of "toastr" non-blocking notification javascript library. |

#### Available Browser-based Components

| Library               | Description                                                                       |
|-----------------------|-----------------------------------------------------------------------------------|
| jquery                | JQuery 3.1.1 facades for Scala.js and ScalaJs.io applications.                    |
| phaser.js             | A fast, free and fun HTML5 Game Framework for Desktop and Mobile web browsers.    |
| pixi.js               | Super fast HTML 5 2D rendering engine that uses webGL with canvas fallback        |

#### Available Social Networking APIs

| SDK                   | Description                                                                       |
|-----------------------|-----------------------------------------------------------------------------------|
| facebook-api          | Facebook SDK integration for Scala.js and ScalaJs.io applications.                |
| linkedin-api          | LinkedIN SDK integration for Scala.js and ScalaJs.io applications.                |


### Server-Side Components

#### Available Node Modules

| Node Module           | Description                                                                     |
|-----------------------|---------------------------------------------------------------------------------|
| assert                | Provides a simple set of assertion tests that can be used to test invariants. |
| buffer                | The Buffer class was introduced as part of the Node.js API to make it possible to interact with octet streams in the context of things like TCP streams and file system operations. |
| child_process         | The child_process module provides the ability to spawn child processes. |
| cluster               | The cluster module allows you to easily create child processes that all share server ports. |
| crypto                | The crypto module provides cryptographic functionality that includes a set of wrappers for OpenSSL's hash, HMAC, cipher, decipher, sign and verify functions.|
| dns                   | Support for DNS queries. | 
| events                | Node.js Events Interface | 
| fs                    | File I/O is provided by simple wrappers around standard POSIX functions. |
| http                  | Node.js HTTP Interface |
| https                 | Node.js HTTPS Interface |
| net                   | The net module provides you with an asynchronous network wrapper. |
| os                    | Provides a few basic operating-system related utility functions. |
| path                  | This module contains utilities for handling and transforming file paths. |
| querystring           | The querystring module provides utilities for parsing and formatting URL query strings. |
| readline              | Readline allows reading of a stream on a line-by-line basis. |
| repl                  | The REPL provides a way to interactively run JavaScript and see the results. | 
| stream                | A stream is an abstract interface implemented by various objects in Node.js. | 
| string-decoder        | The string_decoder module provides an API for decoding Buffer objects into strings in a manner that preserves encoded multi-byte UTF-8 and UTF-16 characters. |
| timers                | The timer module exposes a global API for scheduling functions to be called at some future period of time. |
| tty                   | The tty module provides the tty.ReadStream and tty.WriteStream classes. |
| url                   | The url module provides utilities for URL resolution and parsing. |
| util                  | The util module is primarily designed to support the needs of Node.js's internal APIs.|
| vm                    | The vm module provides APIs for compiling and running code within V8 Virtual Machine contexts.|
| zlib                  | This provides bindings to Gzip/Gunzip, Deflate/Inflate, and DeflateRaw/InflateRaw classes. |

#### Available NPM Packages

| NPM Package                                                                           | Version | Description                                             |
|---------------------------------------------------------------------------------------|---------|---------------------------------------------------------|
| [async](https://github.com/scalajs-io/async)                                          | 2.0.0   | Higher-order functions and common patterns for asynchronous code. |
| [bcrypt](https://github.com/scalajs-io/bcrypt)                                        | 0.0.3   | A native JS bcrypt library for NodeJS. |
| [bignum](https://github.com/scalajs-io/bignum)                                        | 0.12.5  | Arbitrary-precision integer arithmetic using OpenSSL. |
| [body-parser](https://github.com/scalajs-io/body-parser)                              | 1.15.1  | Body parsing middleware. |
| [brake](https://github.com/scalajs-io/brake)                                          | 1.0.1   | Throttle a stream with backpressure. |
| [buffermaker](https://github.com/scalajs-io/buffermaker)                              | 1.2.0   | buffermaker is a convenient way of creating binary strings. |
| [cassandra-driver](https://github.com/scalajs-io/cassandra-driver)                    | 3.0.2   | DataStax Node.js Driver for Apache Cassandra |
| [cheerio](https://github.com/scalajs-io/cheerio)                                      | 0.22.0  | Tiny, fast, and elegant implementation of core jQuery designed specifically for the server |
| [chalk](https://github.com/scalajs-io/chalk)                                          | 1.1.3   | Terminal string styling done right. Much color. |
| [cookie](https://github.com/scalajs-io/cookie)                                        | 0.3.1   | HTTP server cookie parsing and serialization |
| [cookie-parser](https://github.com/scalajs-io/cookie-parser)                          | 1.4.3   | Cookie parsing with signatures |
| [colors](https://github.com/scalajs-io/colors)                                        | 1.1.2   | Get colors in your node.js console.|
| [csv-parse](https://github.com/scalajs-io/csv-parse)                                  | 1.1.2   | CSV parsing implementing the Node.js 'stream.Transform' API.|
| [csvtojson](https://github.com/scalajs-io/csvtojson)                                  | 1.1.4   | A tool concentrating on converting csv data to JSON with customised parser supporting.|
| [drama](https://github.com/scalajs-io/drama)                                          | 0.1.3   | drama is an Actor model implementation for JavaScript and Node.js |
| [escape-html](https://github.com/scalajs-io/escape-html)                              | 1.0.3   | Escape string for use in HTML |
| [express](https://github.com/scalajs-io/express)                                      | 4.13.4  | Fast, unopinionated, minimalist web framework for Node.js |
| [express-csv](https://github.com/scalajs-io/express-csv)                              | 0.6.0   | `express-csv` provides response csv easily to express. |
| [express-fileupload](https://github.com/scalajs-io/express-fileupload)                | 0.0.5   | Simple express file upload middleware that wraps around connect-busboy |
| [express-ws](https://github.com/scalajs-io/express-ws)                                | 2.0.0   | WebSocket endpoints for Express applications |
| [feedparser-promised](https://github.com/scalajs-io/feedparser-promised)              | 1.1.1   | Wrapper around feedparser with promises. |
| [filed](https://github.com/scalajs-io/filed)                                          | 0.1.0   | Simplified file library. |
| [github-api-node](https://github.com/scalajs-io/github-api-node)                      | 0.11.2  | A higher-level wrapper around the Github API. |
| [glob](https://github.com/scalajs-io/glob)                                            | 7.1.1   | A little globber. |
| [html-to-json](https://github.com/scalajs-io/html-to-json)                            | 0.6.0   | Parses HTML strings into objects using flexible, composable filters. |
| [htmlparser2](https://github.com/scalajs-io/htmlparser2)                              | 3.9.1   | A forgiving HTML/XML/RSS parser. The parser can handle streams and provides a callback interface. |
| [jsdom](https://github.com/scalajs-io/jsdom)                                          | 9.9.1   | A JavaScript implementation of the WHATWG DOM and HTML standards, for use with Node.js |
| [jwt-simple](https://github.com/scalajs-io/jwt-simple)                                | 0.5.0   | JWT(JSON Web Token) encode and decode module |
| [kafka-node](https://github.com/scalajs-io/kafka-node)                                | 0.0.11  | A node binding for librdkafka | Tesing required |
| [kafka-rest](https://github.com/scalajs-io/kafka-rest)                                | 0.0.4   | REST Proxy wrapper library for Kafka | Tesing required |
| [md5](https://github.com/scalajs-io/md5)                                              | 2.1.0   | A JavaScript function for hashing messages with MD5. |
| [memory-fs](https://github.com/scalajs-io/memory-fs)                                  | 0.3.0   | A simple in-memory filesystem. Holds data in a javascript object. |
| [mkdirp](https://github.com/scalajs-io/mkdirp)                                        | 0.5.1   | Recursively mkdir, like mkdir -p. |
| [moment](https://github.com/scalajs-io/moment)                                        | 2.17.1  | Parse, validate, manipulate, and display dates in JavaScript. |
| [moment-timezone](https://github.com/scalajs-io/moment)                               | 0.5.11  | Parse and display dates in any timezone. |
| [mongodb](https://github.com/scalajs-io/mongodb)                                      | 2.2.22  | The official MongoDB driver for Node.js. |
| [multer](https://github.com/scalajs-io/multer)                                        | 1.1.0   | Multer is a node.js middleware for handling multipart/form-data. | Tesing required |
| [mysql](https://github.com/scalajs-io/mysql)                                          | 2.10.2  | A node.js driver for mysql. |
| [node-zookeeper-client](https://github.com/scalajs-io/node-zookeeper-client)          | 0.2.2   | A higher-level ZooKeeper client based on node-zookeeper with support for locking and master election. | Tesing required |
| [numeral](https://github.com/scalajs-io/numeral)                                      | 2.0.4   | A javascript library for formatting and manipulating numbers. |
| [oppressor](https://github.com/scalajs-io/oppressor)                                  | 0.0.1   | Streaming http compression response negotiator. | Tesing required |
| [readable-stream](https://github.com/scalajs-io/readable-stream)                      | 2.2.2   | Streams3, a user-land copy of the stream library from Node.js. |
| [request](https://github.com/scalajs-io/request)                                      | 2.72.1  | Simplified HTTP request client. |
| [rxjs](https://github.com/scalajs-io/rxjs)                                            | 4.1.0   | The Reactive Extensions for JavaScript. |
| [socket.io](https://github.com/scalajs-io/socket.io)                                  | 1.7.2   | Realtime application framework (Node.JS server). |
| [socket.io-client](https://github.com/scalajs-io/socket.io-client)                    | 1.7.2   | Socket.io client. |
| [splitargs](https://github.com/scalajs-io/splitargs)                                  | 0.0.7   | Splits strings into tokens by given separator except treating quoted part as a single token. |
| [tingodb](https://github.com/scalajs-io/tingodb)                                      | 0.5.1   | Embedded Node.js database upward compatible with MongoDB. |
| [tough-cookie](https://github.com/scalajs-io/tough-cookie)                            | 2.3.2   | RFC6265 Cookies and Cookie Jar for node.js. |
| [transducers-js](https://github.com/scalajs-io/transducers-js)                        | 0.4.174 | A high performance Transducers implementation for JavaScript. |
| [type-is](https://github.com/scalajs-io/type-is)                                      | 1.6.14  | Infer the content-type of a request. |
| [watch](https://github.com/scalajs-io/watch)                                          | 0.18.0  | Utilities for watching file trees. |
| [winston](https://github.com/scalajs-io/winston)                                      | 2.3.1   | A multi-transport async logging library for Node.js. |
| [winston-daily-rotate-file](https://github.com/scalajs-io/winston-daily-rotate-file)  | 1.4.4   | A multi-transport async logging library for Node.js. |    
| [xml2js](https://github.com/scalajs-io/xml2js)                                        | 0.4.16  | Simple XML to JavaScript object converter. |


### Setup

The initial setup is simple. Just run the provided installer script - `install.py`. It will clone 
the ScalaJ.io repositories for the Core components, browser-based packages and npm packages.

**NOTE**: `install.py` has been provided to both clone the Scalajs.io repositories and keep them updated; 
meaning you'll want to periodically re-run the script to ensure you have the latest code.
However, in order to the script to operate properly, you must first install 
[GitPython](https://github.com/gitpython-developers/GitPython) Python plugin.

### Build Dependencies

* [SBT v0.13.13](http://www.scala-sbt.org/download.html)

### Build/publish the ScalaJs.io platform locally

```bash
 $ sbt clean publish-local
```

### Running the tests

Before running the tests the first time, you must ensure the npm packages are installed:

```bash
$ npm install
```

Then you can run the tests:

```bash
$ sbt test
```

### Artifacts and Resolvers

The following bundled artifacts are generated by this project: 

##### Complete platform bundle

```sbt
libraryDependencies += "io.scalajs" %%% "complete-platform" % "0.3.0.5"
```
##### MEAN Stack bundle (Node, MongoDB and Express.js)

```sbt
libraryDependencies += "io.scalajs.npm" %%% "mean-stack" % "0.3.0.5"
```

##### Angular.js bundle (including all associated components)

```sbt
libraryDependencies += "io.scalajs" %%% "angularjs-bundle" % "0.3.0.5"
```

Optionally, you may add the Sonatype Repository resolver:

```sbt   
resolvers += Resolver.sonatypeRepo("releases") 
```