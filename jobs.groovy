freeStyleJob('MNTLAB-knovichuk-main-build-job'){

  parameters{
      description 'Main job'
  }

}


 for(i=1; i<=4; i++){

  job('MNTLAB-knovichuk-child' + i + '-build-job'){
    
    parameters{
      description ('Child' + i + ' job')
      gitParam('BRANCH_NAME'){
        type('BRANCH')
      }
    }

    } 
  }
