job('MNTLAB-aandryieuski-main-build-job'){
    description('TASK10-main')
    parameters {
        activeChoiceParam('JOBS') {
            description('Choose JOBS')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-aandryieuski-child1-build-job", "MNTLAB-aandryieuski-child2-build-job", "MNTLAB-aandryieuski-child3-build-job", "MNTLAB-aandryieuski-child4-build-job"]')
                fallbackScript('"fallback choice"')
            }
        }
        choiceParam('BRANCH_NAME', ['aandryieuski', 'master'], 'Choose branch')
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
        job("MNTLAB-aandryieuski-child${i}-build-job") {
            description("TASK10-child${i}")
            parameters {
                activeChoiceParam('BRANCH_NAME') {
                    description('Choose branch')
                    filterable()
                    choiceType('SINGLE_SELECT')
                    groovyScript {
                        script('("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute().text.readLines().collect { it.split()[1].replaceAll(\'refs/heads/\', \'\')}.unique()')
                        fallbackScript('"fallback choice"')
                    }
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
                    pattern("output.txt, $BRANCH_NAME_dsl_script.tar.gz")

                }
            }

        }
    }
}
