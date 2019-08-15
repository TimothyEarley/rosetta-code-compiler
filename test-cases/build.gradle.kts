plugins {
	base
	kotlin("jvm")
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform { }
}

dependencies {
	compile(kotlin("stdlib"))
	testImplementation(project(":lexer"))
	testImplementation(project(":syntax"))
	testImplementation(project(":code-gen"))
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")

}