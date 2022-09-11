plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm{}

    sourceSets {
        val coroutinesVersion: String by project
        val commonMain by getting {
            dependencies {
                implementation(project(":easystory-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api(kotlin("test-junit"))
            }
        }
    }
}