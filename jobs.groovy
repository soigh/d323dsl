freeStyleJob('MNTLAB-ypapkou-main-build-job'){
  parameters {
    description 'Main job'
    choiceParam('BRANCH_NAME', ['ypapkou', 'master'], 'Branch name')
    activeChoiceParam('Jobs_to_execute') {
      description('Allows user choose jobs to execute')   	  
      filterable()
      choiceType('CHECKBOX')
      groovyScript {
        script('return ["MNTLAB-ypapkou-child1-build-job", "MNTLAB-ypapkou-child2-build-job", "MNTLAB-ypapkou-child3-build-job", "MNTLAB-ypapkou-child4-build-job"]\n')
     	fallbackScript('"fallback choice"')
      }
    }
  }
}

freeStyleJob('MNTLAB-ypapkou-child1-build-job'){
    description 'Child1 job'
    
}

freeStyleJob('MNTLAB-ypapkou-child2-build-job'){
    description 'Child2 job'
    
}

freeStyleJob('MNTLAB-ypapkou-child3-build-job'){
    description 'Child3 job'
    
}

freeStyleJob('MNTLAB-ypapkou-child4-build-job'){
    description 'Child4 job'
    
}
