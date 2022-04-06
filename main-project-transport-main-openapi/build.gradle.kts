plugins {
    kotlin("jvm") version "1.6.10"
    id("org.openapi.generator") version "5.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/schema.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("org.openapi.example.api")
    invokerPackage.set("org.openapi.example.invoker")
    modelPackage.set("org.openapi.example.model")
}