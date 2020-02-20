import cd.go.contrib.plugins.configrepo.groovy.dsl.*

def branches = ['master', 'release']

def buildStage = {
    new Stage("Build", {
        cleanWorkingDir = true
        jobs {
            job("BuildWebsite") {
                tasks {
                    bash {
                        commandString = "echo job BuildWebsite task1"
                    }
                    bash {
                        commandString = "echo job BuildWebsite task2"
                    }
                }
            }
        }
    })
}

def pushToGHPages = {
    new Stage("PushToGHPages", {
        cleanWorkingDir = true
        jobs {
            job("PushToGHPages") {
                tasks {
                    bash {
                        commandString = "echo job PushToGHPages task1"
                    }
                    bash {
                        commandString = "echo job PushToGHPages task2"
                    }
                }
            }
        }
    })
}

GoCD.script { GoCD buildScript ->

    pipelines {
        branches.each { String branch ->
            pipeline("docs.gocd.org-${branch}") {
                group = "gocd-help-docs-${branch}"
                materials {
                    git {
                        url = 'http://github.com/goelswati/Pipeline-test.git'
                        branch = "${branch}"
                        shallowClone = true
                    }
                }
                stages {
                    add(buildStage())
                    add(pushToGHPages())
                }
            }
        }
    }

    /*environments {
        environment("docs-website") {
            pipelines = buildScript.pipelines.getNames().findAll { !it.toUpperCase().contains('PR') }
        }
    }*/
}
