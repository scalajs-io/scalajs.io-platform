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


# creates the symbolic links for all common repos
def makeSymbolicLinksCommon(repoName):
    for repo_link, local_link in sym_link_files.items():
        repo_link_path = os.path.abspath("{0}/{1}/{2}".format(repo_cache, repoName, repo_link))
        local_link_path = "{0}/{1}".format(repoName, local_link)
        if os.path.exists(repo_link_path) and not os.path.exists(local_link_path):
            os.symlink(repo_link_path, local_link_path)


# creates the symbolic links for the AngularJS repos
def makeSymbolicLinksForAngularJS(repoName):
    angular_components = ["anchor-scroll", "animate", "cookies", "core", "facebook", "md5", "nervgh-fileupload", "nvd3",
                          "sanitize", "toaster", "ui-bootstrap", "ui-router"]
    for comp in angular_components:
        path = "{0}/{1}".format(repoName, comp)
        if not os.path.exists(path): os.mkdir(path)
        makeSymbolicLinksCommon(path)


# creates the symbolic links
def makeSymbolicLinks(repoName):
    switcher = {"angularjs": lambda: makeSymbolicLinksForAngularJS(repoName)}
    func = switcher.get(repoName, lambda: makeSymbolicLinksCommon(repoName))
    func()


#################################################################################################
#       Initial Setup
#################################################################################################

version = "0.3.0.5"

# define the directory for all repositories
repo_cache = "../_repos"

# define the symbolic link files
sym_link_files = {"build.sbt": "build.sbt.txt", "package.json": "package.json", "README.md": "README.md", "src/": "src"}

# define the available repos
repoNames = """angularjs async bcrypt bignum body-parser brake buffermaker
            cassandra-driver chalk cheerio colors cookie cookie-parser core csv-parse
            csvtojson drama dom-html escape-html express express-csv express-fileupload express-ws
            facebook-api feedparser-promised filed github-api-node glob html-to-json htmlparser2
            jquery jsdom jwt-simple kafka-node linkedin-api md5 memory-fs minimist mkdirp 
            moment moment-duration-format moment-timezone mongodb mongoose mpromise multer mysql 
            node-zookeeper-client nodejs numeral oppressor phaser pixijs readable-stream request rx 
            scalajs.io splitargs stream-throttle throttle tingodb tough-cookie transducers-js type-is 
            watch winston winston-daily-rotate-file xml2js""".split(" ")

# convert the array to a list of repo names
repoNames = list(filter(lambda s: not isBlank(s), repoNames))
repoNames = list(map(lambda s: s.strip(), repoNames))

#################################################################################################
#       Application Logic
#################################################################################################

print "ScalaJs.io Complete Platform Build Installer v{0}".format(version)

# create the local repo directory
if not os.path.exists(repo_cache):
    print "Creating the repository cache directory ({0})...".format(repo_cache)
    os.mkdir(repo_cache)

# clone any repos that don't already exist
print "Checking status of {0} repos...".format(len(repoNames))
for repoName in repoNames:
    # does the repo exist?
    repo_dir = "{0}/{1}".format(repo_cache, repoName)
    if os.path.exists(repo_dir):
        # update the repo
        repo = Repo(repo_dir)
        assert not repo.bare
        print "Updating {0}...".format(repoName)
        git = repo.git
        git.pull()
    else:
        # if not, clone the repo
        print "Cloning {0}...".format(repoName)
        git_url = "https://github.com/scalajs-io/{0}".format(repoName)
        Repo.clone_from(git_url, repo_dir)

    # create the local directory if it doesn't exist
    if not os.path.exists(repoName): os.mkdir(repoName)

    # if the local directory now exists, ensure the symbolic links exists
    if os.path.exists(repoName): makeSymbolicLinks(repoName)

print "Done."
