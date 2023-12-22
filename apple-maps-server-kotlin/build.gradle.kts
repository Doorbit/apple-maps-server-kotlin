plugins {
    kotlin("jvm") version "1.9.20"

    `java-library`
    `maven-publish`
}

group = "com.doorbit"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib:1.9.20"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

java {
    withSourcesJar()
}
