
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
    scm {
        git {
            remote {
                github("MNT-Lab/d323dsl", "https")
            }
            branch('$BRANCH_NAME')
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
for(i in 1..4) {
    job("MNTLAB-hviniarski-child${i}-build-job") {

    }
}