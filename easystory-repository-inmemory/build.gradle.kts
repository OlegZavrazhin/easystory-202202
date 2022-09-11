plugins {
    kotlin("multiplatform")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

kotlin {
    jvm {}

    sourceSets {
        val cache4kVersion: String by project
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":easystory-common"))

                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":easystory-repository-test"))
            }
        }
    }
}