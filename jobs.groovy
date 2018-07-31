job("MNTLAB-ymaniukevich-main-build-job") {
	description()
  configure {
    project->
        project / 'properties' << 'hudson.model.ParametersDefinitionProperty' {
        parameterDefinitions {
            'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'BRANCH_NAME'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '1'
                type 'PT_SINGLE_SELECT'
                value "ymaniukevich, master"
                multiSelectDelimiter ','
                projectName "MNTLAB-ymaniukevich-main-build-job"
            }
          'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
                name 'BUILDS_TRIGGER'
                quoteValue 'false'
                saveJSONParameterToFile 'false'
                visibleItemCount '5'
                type 'PT_CHECKBOX'
                groovyScript """return [
					'MNTLAB-ymaniukevich-child1-build-job',
                                        'MNTLAB-ymaniukevich-child2-build-job',
                                        'MNTLAB-ymaniukevich-child3-build-job'
                                        ]"""
                multiSelectDelimiter ','
                projectName "MNTLAB-ymaniukevich-main-build-job"
            }
        }
    }
}
  disabled(false)
	concurrentBuild(false)
	steps {
        downstreamParameterized {
            trigger('$BUILDS_TRIGGER') {
                block {
                    buildStepFailure('never')
                    failure('never')
                    unstable('never')
                }
                parameters {
                    predefinedProp('GIT_BRANCH', '$BRANCH_NAME')
                             }
		}
	}
    }
}


for (i in 1..2){
job("MNTLAB-ymaniukevich-child${i}-build-job") {
	description()
	keepDependencies(false)
 parameters {
        gitParam('sha') {
            description('Revision commit SHA')
            type('BRANCH')
            branch('master')
        }
 }
  scm {
		git {
			remote {
				github("MNT-Lab/d323dsl", "https")
				credentials("b28c8c1e-2ad8-4aa0-b1f5-6caff2756ea9")
			}
			branch("*/master")
		}
	}
}
}
