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

def pushToGHPages(branch) {
    new Stage("PushToGHPages", {
        cleanWorkingDir = true
        jobs {
            job("PushToGHPages") {
                tasks {
                    if (branch == 'master') {
                        bash {
                            commandString = "echo job PushToGHPages task1 with branch master"
                        }
                    } else {
                        bash {
                            commandString = "echo job PushToGHPages task1 with branch release"
                        }
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
            pipeline("pipeline-test${branch == "master" ? '' : "-"+branch}") {
                group = "pipeline-test-${branch}"
                materials {
                    git {
                        url = 'http://github.com/goelswati/Pipeline-test.git'
                        branch = "${branch}"
                        shallowClone = true
                    }
                }
                stages {
                    add(buildStage())
                    add(pushToGHPages(branch))
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
