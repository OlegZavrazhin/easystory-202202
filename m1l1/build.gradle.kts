plugins {
    kotlin("jvm") version "1.6.10"
}

group = "ru.otus.otuskotlin.easystory"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test-junit"))
}