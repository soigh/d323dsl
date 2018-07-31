job('MNTLAB-dzhukova-main-build-job') {
    scm {
        git('https://github.com/MNT-Lab/d323dsl.git')
    }
   
    steps {
       shell('echo "Hello"')
    }
}
