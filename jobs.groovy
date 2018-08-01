job('MNTLAB-ysokal-main-build-job'){
    description 'This is the simple main Job'
    parameters {
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Select the jobs')
            choiceType('CHECKBOX')
            groovyScript {
                script('''list = []
for(i in 1..4){
  list.add("MNTLAB-ysokal-child${i}-build-job")
    }
return list\n''')

            }
        }
        choiceParam('BRANCH_NAME', ['ysokal', 'master'], 'Choose branch from git "d323dsl".')

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
                    currentBuildParameters()
                }
            }
        }
    }
}
for(i in 1..4){
    job("MNTLAB-ysokal-child${i}-build-job"){
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                description('Select the branch')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute().text.readLines().collect { it.split()[1].replaceAll(\'refs/heads/\', \'\') }.unique()')

                }
            }
        }
        scm {
            github ('MNT-Lab/d323dsl', '${BRANCH_NAME}')
        }
        wrappers {
            preBuildCleanup {
                deleteDirectories(false)
                cleanupParameter()
            }
        }
        steps{
            shell('bash script.sh > output.txt\ntar -czf ${BRANCH_NAME}_dsl_script.tar.gz *')
        }
        publishers {
            archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
        }
    }
}