import cd.go.contrib.plugins.configrepo.groovy.dsl.*

def branches = ['master', 'release']

def build = {
    new Stage("Build", {
        cleanWorkingDir = true
        jobs {
            job("build") {
                tasks {
                    bash {
                        commandString = "echo job build task1"
                    }
                    bash {
                        commandString = "echo job build task2"
                    }
                }
            }
        }
    })
}

/*def static publish(String branch) {
    new Stage("publish", {
        cleanWorkingDir = true
        jobs {
            job("publish") {
                tasks {
                    bash {
                        commandString = "echo branch name ${branch}"
                    }
                    bash {
                        commandString = "echo job publish task1"
                    }
                    bash {
                        commandString = "echo job publish task2"
                    }
                }
            }
        }
    })
}*/

GoCD.script { GoCD buildScript ->

    pipelines {
        branches.each { String branch ->
            pipeline("pipeline-test${branch == "master" ? '' : "-"+branch}") {
                group = "pipeline-test-${branch}"
                materials {
                    git {
                        url = 'http://github.com/goelswati/Pipeline-test.git'
                        branch = branch
                        shallowClone = true
                    }
                }
                stages {
                    add(build())
                    stage('publish') {
                        cleanWorkingDir = true
                        approval {
                            type = branch == "master" ? '' : 'manual'
                        }
            jobs {
            job("publish") {
                artifacts {
                    build {
                        source = branch == "master" ? capital_image_ref_master : capital_image_ref_releases
                    }
                }
                tasks {
                    bash {
                        commandString = "echo branch name ${branch}"
                    }
                    bash {
                        commandString = "echo job publish task1"
                    }
                    bash {
                        commandString = "echo job publish task2"
                    }
                }
            }
        }
        }
                }
            }
        }
    }
}
