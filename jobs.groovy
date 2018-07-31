folder('dzhukova')
job('dzhukova/MNTLAB-dzhukova-main-build-job') {
  scm {
        git {
            remote {
                github("MNT-Lab/d323dsl", "https")
                
            }
          branch('$branch')
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
                visibleItemCount '15'
                type 'PT_MULTI_SELECT'
                value 'MNTLAB-dzhukova-child1-build-job, MNTLAB-dzhukova-child2-build-job, MNTLAB-dzhukova-child3-build-job, MNTLAB-dzhukova-child4-build-job'
                multiSelectDelimiter ','
                projectName "MNTLAB-dzhukova-main-build-job"
            }
        
        }
        }
}
          
     parameters {

      gitParameterDefinition {
        name("branch")
        type("PT_BRANCH")
        defaultValue("origin/dzhukova")
        description("choose branch")
        branch('')
        branchFilter("origin/dzhukova|origin/master")
        tagFilter('*')
        sortMode('NONE')
        selectedValue('NONE')
        useRepository('https://github.com/MNT-Lab/d323dsl.git')
        quickFilterEnabled(false)
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
                    currentBuild()
                }
            }
            
        }
   
   
    }
}
job('dzhukova/MNTLAB-dzhukova-child1-build-job') {
    
    steps {
       shell('echo "Hello1"')
    }
}
job('dzhukova/MNTLAB-dzhukova-child2-build-job') {
    
    steps {
       shell('echo "Hello2"')
    }
}
job('dzhukova/MNTLAB-dzhukova-child3-build-job') {
    
    steps {
       shell('echo "Hello3"')
    }
}
job('dzhukova/MNTLAB-dzhukova-child4-build-job') {
    
    steps {
       shell('echo "Hello4"')
    }
}
