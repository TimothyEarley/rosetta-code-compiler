plugins {
	base
	kotlin("jvm")
}

tasks.getting(Test::class) {
	useJUnitPlatform { }
}

dependencies {
	compile(kotlin("stdlib"))
	testImplementation(project(":lexer"))
	testImplementation(project(":syntax"))
	testImplementation(project(":code-gen"))
	testImplementation(project(":vm"))
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")

}