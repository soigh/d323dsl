def git_info = ("git ls-remote -h https://github.com/soigh/d323dsl.git").execute()
// доступ к удаленному репозиторию
def branches = git_info.text.readLines().collect { it.split()[1].replaceAll('refs/heads/', '')}.unique()
// список бранчей

job('MNTLAB-osamorukova-main-build-job') {
	parameters {
		choiceParam('BRANCH_NAME', ["osamorukova", "master"], 'Branches')
// создание параметризированной джобы, параметр BRANCH_NAME, варианты: osamorukova, master
		activeChoiceParam('BUILD_TRIGGER') {
			description('Available options')
			choiceType('CHECKBOX')
			groovyScript {
				script('''return ["MNTLAB-osamorukova-child1-build-job",
"MNTLAB-osamorukova-child2-build-job",
"MNTLAB-osamorukova-child3-build-job",
"MNTLAB-osamorukova-child4-build-job"]''')
				fallbackScript('return [error]')
// параметр BUILD_TRIGGER, мультивыбор из списка дочерних джоб
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
// запуск нового параметризированного билда
				}
			}
		}
	}
}

for (i in 1..4) { 
// цикл для перебора элементов списка
	job("MNTLAB-osamorukova-child${i}-build-job") {
		parameters {
			choiceParam('BRANCH_NAME', branches, '')
		}
// создание дочерних параметризированных джоб
		steps {
			shell('''./script.sh > output.txt
tar -czf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy''')
		}
		publishers {
			archiveArtifacts {
				pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')
				allowEmpty(false)
				onlyIfSuccessful(true)
				fingerprint(false)
				defaultExcludes(true)
/* шаг билда - исполнение скрипта и его вывод в текстовый файл, архивирование jobs.groovy,
также создание артифактов джобы */ 
			}
		}
	}
}
