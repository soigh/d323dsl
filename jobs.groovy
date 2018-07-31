import jenkins.model.*
import hudson.model.*
def repo_data = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = repo_data.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}
//def all_jobs = Jenkins.instance.getAllItems(AbstractItem.class)
job('MNTLAB-stsitou-main-build-job'){
    description('main')
    parameters {
        activeChoiceParam('JOBS') {
            description('Choose a job')
            filterable(false)
            choiceType('CHECKBOX')
            groovyScript {
                script('return Jenkins.instance.getAllItems(AbstractItem.class)')
            }
        }
        choiceParam('BRANCH_NAME', branches, 'Choose a branch')
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
        job("MNTLAB-stsitou-child${i}-build-job") {
            description("Child${i}")
            parameters {
                activeChoiceParam('BRANCH_NAME') {
                    description('Choose a branch')
                    filterable(false)
                    choiceType('SINGLE_SELECT')
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
                    pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')

                }
            }

        }
    }
}