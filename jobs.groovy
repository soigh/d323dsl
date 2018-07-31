job("MNTLAB-mpiatliou-main-build-job") {
    parameters {
        choiceParam('BRANCH_NAME', ['master', 'mpiatliou'], 'Branch choice')
        activeChoiceParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            description('Available options')
            groovyScript {
                script('''return ["MNTLAB-mpiatliou-child1-build-job","MNTLAB-mpiatliou-child2-build-job","MNTLAB-mpiatliou-child3-build-job","MNTLAB-mpiatliou-child4-build-job"]''')
                fallbackScript('"fallback choice"')
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
    }
}
    steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
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
