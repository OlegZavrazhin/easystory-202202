plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

tasks {
    withType<Test> {
        environment("es.sql_drop_db", true)
        environment("es.sql_fast_migration", true)
    }
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val testContainerVersion: String by project
    implementation(kotlin("stdlib"))
    implementation(project(":easystory-common"))
    implementation(project(":easystory-repository-test"))
    implementation("org.postgresql:postgresql:$postgresDriverVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

    testImplementation("org.testcontainers:postgresql:$testContainerVersion")
    testImplementation(project(":easystory-repository-test"))
}