plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
//    macosX64 {}
//    linuxX64 {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":easystory-common"))
                implementation(project(":easystory-stubs"))
                implementation(project(":easystory-business-logic"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}