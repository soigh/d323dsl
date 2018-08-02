def git_info = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute()
def branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.unique()

job('MNTLAB-mznak-main-build-job'){
   scm {
        github('MNT-Lab/d323dsl', '$BRANCH_NAME')
    }
  
  configure {
    project->
        project / 'properties' << 'hudson.model.ParametersDefinitionProperty' {
        parameterDefinitions {
            'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'JOBS'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '5'
                type 'PT_CHECKBOX'
                value '''MNTLAB-mznak-child1-build-job,
                        MNTLAB-mznak-child2-build-job,
                        MNTLAB-mznak-child3-build-job,
                        MNTLAB-mznak-child4-build-job'''
                multiSelectDelimiter ','
                projectName "MNTLAB-mznak-main-build-job"
            }
	   'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'BRANCH_NAME'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '1'
                type 'PT_SINGLE_SELECT'
                value "mznak, master"
                multiSelectDelimiter ','
                projectName "MNTLAB-mznak-main-build-job"
           }
        }
     }
  }
  
 /* parameters{
      gitParameterDefinition{
        name('BRANCH_NAME')
        type('BRANCH')
        defaultValue('origin/mznak')
        description('')
        branch('') 
        branchFilter('origin/(master|mznak)')
        tagFilter('.*')
        sortMode('NONE')
        selectedValue('TOP')
        useRepository('')
        quickFilterEnabled(false)
        listSize('1')
      }
  }*/
  steps {
        downstreamParameterized {
            trigger('$JOBS') {
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

for(i in 1..4){
  job('MNTLAB-mznak-child'+i+'-build-job'){
    /* scm {
      git {
        remote {
          url('https://github.com/MNT-Lab/d323dsl.git')
        }
        branch('$BRANCH_NAME')
      }
    }
    parameters {
      choiceParam('BRANCH_NAME', branches, '')
    }*/
	  
    activeChoiceParam('BRANCH_NAME') {
    	description('Name')
    	filterable(false)
    	choiceType('SINGLE_SELECT')
    	groovyScript {
    		script('def git_info = ("git ls-remote -h https://github.com/MNT-Lab/d323dsl").execute(); return branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}')
    			fallbackScript('"fallback choice"')
    }
        }
    steps {
        shell('''/bin/bash $WORKSPACE/script.sh > $WORKSPACE/output.txt;
				 name=$( echo $BRANCH_NAME | cut -d'/' -f2)
				 tar -czvf "$name"_dsl_script.tar.gz -C $WORKSPACE jobs.groovy''')
    }     
   /* scm {
        github('MNT-Lab/d323dsl', '$BRANCH_NAME')
    }*/
    publishers {
      archiveArtifacts('output.txt, *_dsl_script.tar.gz')
    }
  }
}
