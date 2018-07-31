folder('dzhukova')
job('dzhukova/MNTLAB-dzhukova-main-build-job') {
    scm {
        git('https://github.com/MNT-Lab/d323dsl.git')
       
    }
parameters {
  
      gitParameterDefinition {
      	name("choose branch")
      	type("PT_BRANCH")
      	defaultValue("origin/dzhukova")
      	description("choose branch")
      	branch('*')
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
