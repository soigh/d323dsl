import jenkins.model.*
import hudson.model.*
def repo_data = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = repo_data.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}


job('MNTLAB-stsitou-main-build-job'){
    description('main')
    parameters {
        activeChoiceParam('JOBS') {
            description('Choose jobs to run')
            filterable(false)
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["1","2"]')
            }
        }
        choiceParam('BRANCH_NAME', branches, 'Choose a branch')
    }
    concurrentBuild(false)
    steps {
        downstreamParameterized {
            trigger('$JOBS') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    currentBuildParameters()
                }
            }
        }
    }

    for(i in 1..4) {
        job("MNTLAB-stsitou-child${i}-build-job") {
            description("Child${i}")
            parameters {
                choiceParam('BRANCH_NAME', branches, 'Choose a branch')
            }
            scm {
                git {
                    remote {
                        github("MNT-Lab/d323dsl", "https")
                    }
                    branch('$BRANCH_NAME')
                }
            }
            steps {
                shell('chmod +x ./script.sh; ./script.sh > output.txt; tar -czvf $BRANCH_NAME\\_dsl_script.tar.gz jobs.groovy')
            }
            publishers {
                archiveArtifacts {
                    pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')

                }
            }

        }
    }
}