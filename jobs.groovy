job('MNTLAB-aandryieuski-main-build-job'){
    description('TASK10-main')
    parameters {
        activeChoiceParam('JOBS') {
            description('Choose JOBS')
            filterable(false)
            choiceType('CHECKBOX')
            groovyScript {
                script('list = []; for(i in 1..4){ list.add("MNTLAB-aandryieuski-child${i}-build-job")}; return list\n')

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
                    filterable(false)
                    choiceType('SINGLE_SELECT')
                    groovyScript {
                        script('("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute().text.readLines().collect { it.split()[1].replaceAll(\'refs/heads/\', \'\')}.unique()')

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
            wrappers {
                preBuildCleanup {
                    deleteDirectories(false)
                    cleanupParameter()
                }
            }
            steps {
                shell('''sh script.sh > output.txt; tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz jobs.groovy''')
            }
            publishers {
                archiveArtifacts {
                    pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')

                }
            }

        }
    }
}
