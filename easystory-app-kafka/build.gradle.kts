plugins {
    kotlin("jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

group = rootProject.group
version = rootProject.version

application {
    mainClass.set("ru.otus.otuskotlin.easystory.kafka.MainKt")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk17:alpine-jre")
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest"
            )
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val kafkaTestContainerVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val atomicfuVersion: String by project


    implementation(kotlin("stdlib"))
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    implementation(project(":easystory-common"))
    implementation(project(":easystory-api-jackson"))
    implementation(project(":easystory-mappers-v1"))
    implementation(project(":easystory-services"))
    implementation(project(":easystory-stubs"))
    implementation(project(":easystory-business-logic"))

    testImplementation(kotlin("test-junit"))
    testImplementation("org.testcontainers:kafka:$kafkaTestContainerVersion")
}