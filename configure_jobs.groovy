for (i in 1..2) {
	freeStyleJob("MNTLAB-ymaniukevich-child${i}-build-job") {
    	description("It is child")
		label('test${i}')
    }
}
