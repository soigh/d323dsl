job("MNTLAB-hviniarski-main-build-job") {
    description()
    keepDependencies(false)
    parameters {
        choiceParam('BRANCH_NAME', ['hviniarski', 'master'], 'Branch choice')
        activeChoiceParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ["MNTLAB-hviniarski-child1-build-job","MNTLAB-hviniarski-child2-build-job","MNTLAB-hviniarski-child3-build-job","MNTLAB-hviniarski-child4-build-job"]''')
                fallbackScript('"fallback choice"')
            }
    }
    steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
            }
        }
    }
    disabled(false)
    concurrentBuild(false)
}
def git_info = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.unique()

for(i in 1..4) {
    job("MNTLAB-hviniarski-child${i}-build-job") {
        parameters {
            choiceParam('BRANCH_NAME', branches, 'Branch choice')
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
            shell('bash script.sh > output.txt')
            shell('tar cvf $BRANCH_NAME\\_dsl_script.tar.gz jobs.groovy')
        }
        publishers {
            archiveArtifacts {
                pattern("output.txt, *.tar.gz")
                allowEmpty(false)
                onlyIfSuccessful(false)
                fingerprint(false)
                defaultExcludes(true)
            }
        }
    }
}