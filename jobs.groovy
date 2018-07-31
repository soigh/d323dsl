job('MNTLAB-omonko-main-build-job') {
    description 'Main job'
    parameters {
    	choiceParam('BRANCH_NAME', ['omonko (default)', 'master'], 'Branch name')
        activeChoiceParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ["MNTLAB-omonko-child1-build-job", 
                       "MNTLAB-omonko-child2-build-job", 
                       "MNTLAB-omonko-child3-build-job", 
                       "MNTLAB-omonko-child4-build-job"]
''')
                fallbackScript('"fallback choice"')
            }
        }
    }
    scm {
       
    }
    steps {
      	dsl {
          job('MNTLAB-omonko-child1-build-job') {}
          job('MNTLAB-omonko-child2-build-job') {}
          job('MNTLAB-omonko-child3-build-job') {}
          job('MNTLAB-omonko-child4-build-job') {}
          	
        }
        
    }
    publishers {
    }
}
