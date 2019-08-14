plugins {
    base
    kotlin("jvm") version "1.3.41"
}

allprojects {
    group = "de.earley.rosetta-code-compiler"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
