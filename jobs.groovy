job("MNTLAB-mpiatliou-main-build-job") {
    description()
    keepDependencies(false)
    parameters {
        choiceParam('BRANCH_NAME', ['master', 'mpiatliou'], 'Branch choice')
        activeChoiceParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ["MNTLAB-mpiatliou-child1-build-job","MNTLAB-mpiatliou-child2-build-job","MNTLAB-mpiatliou-child3-build-job","MNTLAB-mpiatliou-child4-build-job"]''')
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
    }
    for(i in 1..4) {
        job("MNTLAB-mpiatliou-child${i}-build-job") {

        }
    }
}
