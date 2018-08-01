def git = 'MNT-Lab/d323dsl'
def repo = '$BRANCH_NAME'
def gitURL = "https://github.com/MNT-Lab/d323dsl.git"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()

if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}

def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}

job('MNTLAB-disakau-main-build-job') {
    scm {
        github(git, repo)
    }
    parameters {
        choiceParam('BRANCH_NAME', ['disakau', 'master'])

        activeChoiceParam('BUILDS_TRIGGER') {
            description('Choose: ')
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-disakau-child-1-build-job", "MNTLAB-disakau-child-2-build-job", "MNTLAB-disakau-child-3-build-job", "MNTLAB-disakau-child-4-build-job"]')
            }
        }
    }
    steps {
        downstreamParameterized {
            trigger('$BUILDS_TRIGGER') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    currentBuild()
                }
            }
        }
    }

    (1..4).each {
        println "Job Number: ${it}"
        job("MNTLAB-disakau-child-${it}-build-job") {
            scm {
                github(git, repo)
            }
            parameters { choiceParam('BRANCH_NAME', branches) }
            steps {
                shell('chmod +x ./script.sh; ./script.sh; ./script.sh >> output.txt; tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh jobs.groovy')
            }
            publishers {
                archiveArtifacts {
                    pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                    pattern('output.txt')
                    onlyIfSuccessful()
                }
            }
        }
    }
}