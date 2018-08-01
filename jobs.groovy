joblist = [];

for(i=1; i<=4; i++){

    jobname = 'MNTLAB-knovichuk-child' + i + '-build-job'

    job(jobname){

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

    joblist.add('"' + jobname + '"')
}


freeStyleJob('MNTLAB-knovichuk-main-build-job'){

  scm {
      github('MNT-Lab/d323dsl', '$BRANCH_NAME')
  }

  parameters{
      description 'Main job'
      activeChoiceParam('Jobs') {
            description('Select a job to execute')
            choiceType('CHECKBOX')
            groovyScript {
                script(joblist.toString())
                fallbackScript('return ["ERROR"]')
            }
      }
  }

}