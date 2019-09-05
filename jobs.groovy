def git_info = ("git ls-remote -h https://github.com/soigh/d323dsl.git").execute()
def branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.unique()

for (i in 1..4) {
  job("MNTLAB-osamorukova-child${i}-build-job") {
    scm {
      git {
        remote {
          url('https://github.com/soigh/d323dsl.git')
        }
        branch('$BRANCH_NAME')
      }
    }
    parameters {
      choiceParam('BRANCH_NAME', branches, '')
    }
    steps {
      shell('''
chmod +x script.sh
./script.sh > output.txt
tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy''')
    }
    publishers {
		archiveArtifacts {
          pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')
			allowEmpty(false)
			onlyIfSuccessful(false)
			fingerprint(false)
			defaultExcludes(true)
		}
	}
  }
}

job('MNTLAB-osamorukova-main-build-job') {
    scm {
      git {
        remote {
          url('https://github.com/soigh/d323dsl.git')
        }
        branch('$BRANCH_NAME')
      }
    }
    parameters {
        choiceParam('BRANCH_NAME', ["osamorukova", "master"], '')
        activeChoiceParam('BUILD_TRIGGER') {
            description('Available options')
            choiceType('CHECKBOX')
            groovyScript {
              script('''return ["MNTLAB-osamorukova-child1-build-job",
"MNTLAB-osamorukova-child2-build-job",
"MNTLAB-osamorukova-child3-build-job",
"MNTLAB-osamorukova-child4-build-job"]''')
              fallbackScript('"fallback choice"')
            }
        }
        
    }
    steps {
		downstreamParameterized {
			trigger('$BUILD_TRIGGER') {
				block {
					buildStepFailure("FAILURE")
					unstable("UNSTABLE")
					failure("FAILURE")
				}
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
			}
		}
	}
}
