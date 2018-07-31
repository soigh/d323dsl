job('MNTLAB-omonko-main-build-job') {
    description 'Main job'
    parameters {
    	choiceParam('BRANCH_NAME', ['omonko (default)', 'master'], 'Branch name')
        activeChoiceParam('choice1') {
                      description('select your choice')
                      choiceType('RADIO')
                      groovyScript {
                          script("return ['aaa', 'bbb']")
                      }
                  }
    }
    scm {
       
    }
    steps {
      	dsl {
          	job('MNTLAB-omonko-child1-build-job')
          	
        }
        
    }
    publishers {
    }
}
