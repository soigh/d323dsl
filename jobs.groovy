job('MNTLAB-omonko-main-build-job') {
    description 'Main job'
    parameters {
    	choiceParam('BRANCH_NAME', ['omonko', 'master'], 'Branch name')
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
    publishers {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
                condition('UNSTABLE_OR_BETTER')
                parameters {
                    currentBuild()
                }
            }
        }
    }
}
for (i in 1..4) {
   job("MNTLAB-omonko-child${i}-build-job") {
     	scm {
           git {
           		remote {
                	url('https://github.com/MNT-Lab/d323dsl.git')
            	}
             	
           }
       
    	}
   		parameters {
        	gitParam('BRANCH_NAME') {
            	type('BRANCH')
            }
    	}
        steps {
            shell("""chmod +x script.sh
                ./script.sh > output.txt""")
        }
     	publishers {
			archiveArtifacts {
				pattern("output.txt")
            }
        }
     
    }  
}
