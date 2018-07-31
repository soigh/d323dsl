freeStyleJob('MNTLAB-knovichuk-main-build-job'){

  scm {
      github('MNT-Lab/d323dsl', '$BRANCH_NAME')
  }

  parameters{
      description 'Main job'
  }

}


for(i=1; i<=4; i++){

  job('MNTLAB-knovichuk-child' + i + '-build-job'){

    scm {
      github('MNT-Lab/d323dsl', '$BRANCH_NAME')
    }
    
    parameters{
      description ('Child' + i + ' job')
      gitParam('BRANCH_NAME'){
        type('BRANCH')
      }
    }

    } 
  }
