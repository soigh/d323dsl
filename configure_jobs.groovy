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
}
