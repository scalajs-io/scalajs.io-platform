#!/usr/bin/python
#
# ScalaJs.io Complete Platform Build Installer
# author: lawrence.daniels@gmail.com

import os
from git import Repo


#################################################################################################
#       Methods
#################################################################################################

# determines whether a string is blank
def isBlank(s): return not (s and s.strip())


# creates the symbolic links
def makeSymbolicLinks(repo):
    for repo_link, local_link in sym_link_files.items():
        repo_link_path = os.path.abspath("{0}/{1}/{2}".format(repo_cache, repo, repo_link))
        local_link_path = "{0}/{1}".format(repo, local_link)
        if os.path.exists(repo_link_path) and not os.path.exists(local_link_path):
            print "{0]: updating symbolic links".format(repo)
            os.symlink(repo_link_path, local_link_path)


# creates the symbolic links for the AngularJS repos
def makeSymbolicLinksForAngulaJS():
    angular_components = ["anchor-scroll", "animate", "cookies", "core", "facebook", "md5", "nervgh-fileupload", "nvd3",
                          "sanitize", "toaster", "ui-bootstrap", "ui-router"]
    for comp in angular_components:
        makeSymbolicLinks("angularjs/{0}".format(comp))


# creates the symbolic links for the ScalaJs.io repos
def makeSymbolLinksForScalaJsIO():
    scalajs_io_comonents = ["core", "dom_html", "nodejs"]
    for comp in scalajs_io_comonents:
        makeSymbolicLinks("scalajs.io/{0}".format(comp))


#################################################################################################
#       Initial Setup
#################################################################################################

version = "0.3.0.5"

# define the directory for all repositories
repo_cache = "../_repos"

# define the repo handler
switcher = {
    "angularjs": lambda: makeSymbolicLinksForAngulaJS(),
    "scalajs.io": lambda: makeSymbolLinksForScalaJsIO()
}

# define the symbolic link files
sym_link_files = {"build.sbt": "build.sbt.txt", "package.json": "package.json", "README.md": "README.md", "src/": "src"}

# define the available repos
repos = """angularjs async bcrypt bignum body-parser brake buffermaker
            cassandra-driver chalk cheerio colors cookie cookie-parser csv-parse
            csvtojson drama escape-html express express-csv express-fileupload express-ws
            facebook-api feedparser-promised filed github-api-node glob html-to-json htmlparser2
            jquery jsdom jwt-simple kafka-node linkedin-api md5 memory-fs minimist mkdirp
            moment moment-timezone mongodb multer mysql node-zookeeper-client
            numeral oppressor phaser pixijs readable-stream request rx scalajs.io splitargs
            tingodb tough-cookie transducers-js type-is watch winston winston-daily-rotate-file 
            xml2js""".split(" ")

# convert the array to a list of repo names
repos = list(filter(lambda s: not isBlank(s), repos))
repos = list(map(lambda s: s.strip(), repos))

#################################################################################################
#       Application Logic
#################################################################################################

print "ScalaJs.io Complete Platform Build Installer v{0}".format(version)

# create the local repo directory
if not os.path.exists(repo_cache):
    print "Creating the repository cache directory ({0})...".format(repo_cache)
    os.mkdir(repo_cache)

# clone any repos that don't already exist
print "Checking status of {0} repos...".format(len(repos))
for repo in repos:
    # does the repo exist?
    # if not, clone the repo
    repo_dir = "{0}/{1}".format(repo_cache, repo)
    if not os.path.exists(repo_dir):
        print "Cloning {0}...".format(repo)
        git_url = "https://github.com/scalajs-io/{0}".format(repo)
        Repo.clone_from(git_url, repo_dir)

    # create the local directory if it doesn't exist
    if not os.path.exists(repo): os.mkdir(repo)

    # if the local directory now exists, ensure the symbolic links exists
    if os.path.exists(repo):
        func = switcher.get(repo, lambda: makeSymbolicLinks(repo))
        func()

print "Done."
