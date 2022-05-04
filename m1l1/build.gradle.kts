plugins {
    kotlin("jvm")
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