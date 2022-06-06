import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project

fun DependencyHandler.ktor(module: String, prefix: String = "server-", version: String? = ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("OZ")
        ports.set(listOf(8080))
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
    val logbackVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(ktor("core"))
    implementation(ktor("netty"))
    implementation(ktor("jackson", "serialization"))
    implementation(ktor("content-negotiation"))
    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktor("call-logging"))
    implementation(ktor("default-headers"))
    implementation(ktor("cors"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("websockets"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(project(":easystory-common"))
    implementation(project(":easystory-api-jackson"))
    implementation(project(":easystory-mappers-v1"))
    implementation(project(":easystory-services"))
    implementation(project(":easystory-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation(ktor("test-host"))
    testImplementation(ktor("content-negotiation", prefix = "client-"))

}
