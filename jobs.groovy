folder('dzhukova')
job('dzhukova/MNTLAB-dzhukova-main-build-job') {
  scm {
        git {
            remote {
                github("MNT-Lab/d323dsl", "https")
                
            }
          branch('$BRANCH_NAME')
        }
  }
  
configure {
    project->
        project / 'properties' << 'hudson.model.ParametersDefinitionProperty' {
        parameterDefinitions {
            'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'jobs'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '5'
                type 'PT_MULTI_SELECT'
                value 'MNTLAB-dzhukova-child1-build-job, MNTLAB-dzhukova-child2-build-job, MNTLAB-dzhukova-child3-build-job, MNTLAB-dzhukova-child4-build-job'
                multiSelectDelimiter ','
                projectName "MNTLAB-dzhukova-main-build-job"
            }
        
        'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'BRANCH_NAME'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '2'
                type 'PT_SINGLE_SELECT'
                value "dzhukova, master"
                multiSelectDelimiter ','
                projectName "MNTLAB-dzhukova-main-build-job"
}
        }
        }
}
          
    
 steps {
       shell('echo "Hello"')
   downstreamParameterized {
     trigger('$jobs') {
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
def git_info = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.unique()
for(i in 1..4) {
  job("dzhukova/MNTLAB-dzhukova-child${i}-build-job") {
  parameters {
            choiceParam('BRANCH_NAME', branches, 'Git branch choice')
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
      shell('echo "Hello${i}"')
      shell('bash script.sh > output.txt')
      shell('tar cvf "$BRANCH_NAME"_dsl_script.tar.gz jobs.groovy')
      
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
