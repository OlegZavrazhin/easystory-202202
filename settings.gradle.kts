rootProject.name = "easystory-202202"

pluginManagement {
    plugins {
//        settings are being set in gradle.properties
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        val openapiVersion: String by settings
//        val serializationVersion: String by settings

//        apply false means not to apply to current project and use the plugins in subprojects without version
        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("m1l1")
//include("m1l2")
//include("m1l4-dsl")
include("easystory-api-kmp")
include("easystory-api-jackson")
