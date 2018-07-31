def repo_data = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = repo_data.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}

job('MNTLAB-stsitou-main-build-job'){
    description('TASK10-main')
    parameters {
        activeChoiceParam('JOBS') {
            description('Choose a job')
            filterable(false)
            choiceType('CHECKBOX')
            groovyScript {
                script('return ["MNTLAB-stsitou-child1-build-job",' +
                        '"MNTLAB-stsitou-child2-build-job",' +
                        '"MNTLAB-stsitou-child3-build-job",' +
                        '"MNTLAB-stsitou-child4-build-job"]')

            }
        }
        choiceParam('BRANCH_NAME', branches, 'Choose a branch')
    }
    disabled(false)
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
                activeChoiceParam('BRANCH_NAME') {
                    description('Choose a branch')
                    filterable(false)
                    choiceType('SINGLE_SELECT')
                }
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
                shell(' ./script.sh > output.txt; tar -czvf $BRANCH_NAME\\_dsl_script.tar.gz jobs.groovy')
            }
            publishers {
                archiveArtifacts {
                    pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')

                }
            }

        }
    }
}