plugins {
	base
	kotlin("jvm")
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform { }
}

dependencies {
	compile(kotlin("stdlib-jdk8"))
	testImplementation(project(":code-gen"))
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
}