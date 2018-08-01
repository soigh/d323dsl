job("MNTLAB-mpiatliou-main-build-job") {
    parameters {
        choiceParam('BRANCH_NAME', ['master', 'mpiatliou'], 'branch choice')
        activeChoiceParam('JOB_TO_BUILD') {
            choiceType('CHECKBOX')
            description('options')
            groovyScript {
                script('''return [
"MNTLAB-mpiatliou-child1-build-job",
"MNTLAB-mpiatliou-child2-build-job",
"MNTLAB-mpiatliou-child3-build-job",
"MNTLAB-mpiatliou-child4-build-job"]''')
                fallbackScript('"fallback choice"')
            }
        }
    }
    scm {
        git {
            remote {
                url('https://github.com/MNT-Lab/d323dsl.git')
            }
            branch('$BRANCH_NAME')
        }
    }
    steps {
        downstreamParameterized {
            trigger('$JOB_TO_BUILD') {
                block {
                    buildStepFailure("FAILURE")
                    unstable("UNSTABLE")
                    failure("FAILURE")
                }
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
        }
    }
}

for(i in 1..4) {
    job("MNTLAB-mpiatliou-child${i}-build-job") {
        wrappers {
            preBuildCleanup()
        }
        parameters {
            choiceParam('BRANCH_NAME', ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").
                    execute().text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.sort(), '')
        }
        scm {
            git {
                remote {
                    url('https://github.com/MNT-Lab/d323dsl.git')
                }
                branch('$BRANCH_NAME')
            }
        }
        steps {
            shell('''chmod +x script.sh; ./script.sh > output.txt; tar -cvzf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy''')
        }
        publishers {
            archiveArtifacts {
                pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')
                allowEmpty(false)
                onlyIfSuccessful(false)
                fingerprint(false)
                defaultExcludes(true)
            }
        }
    }
}
