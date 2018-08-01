def giturl = 'https://github.com/MNT-Lab/d323dsl.git'
def git_info = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = git_info.text.readLines().collect{it.split()[1].replaceAll('refs/heads/', '')}.sort()
  
job ("MNTLAB-akavaleu-main-build-job") {

    parameters {
        choiceParam('BRANCH_NAME', ['akavaleu', 'master'], 'Branch name')
        activeChoiceParam('Next_job') {
            description('Choose branches')
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ["MNTLAB-akavaleu-child1-build-job",
                        "MNTLAB-akavaleu-child2-build-job",
                        "MNTLAB-akavaleu-child3-build-job",
                        "MNTLAB-akavaleu-child4-build-job"]''')
                fallbackScript('return ["Script error!"]')
            }
        }
    }

    steps {
        downstreamParameterized {
            trigger('$Next_job') {
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

for (i in (1..4)) {
        job("MNTLAB-akavaleu-child${i}-build-job") {

            scm {
                git(giturl)
            }

            parameters {
                activeChoiceParam('BRANCH_NAME') {
                    description('Choose branches')
                    choiceType('SINGLE_SELECT')
                    groovyScript {
                      script("${branches}")
                        fallbackScript('"fallback choice"')
                    }
                }
            }

            steps {
                shell('''
   bash script.sh > output.txt
   tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy''')
            }
            publishers {
                archiveArtifacts {
                    pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                    allowEmpty(false)
                    onlyIfSuccessful(false)
                    fingerprint(false)
                    defaultExcludes(true)
                }
            }
        }
}
