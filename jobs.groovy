
va')
job('dzhukova/MNTLAB-dzhukova-main-build-job') {
    scm {
        git('https://github.com/MNT-Lab/d323dsl.git')
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
