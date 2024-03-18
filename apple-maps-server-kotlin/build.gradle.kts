import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.vanniktech.maven.publish") version "0.26.0"
    id("org.jetbrains.dokka") version "1.9.10"

    `java-library`
}

group = "com.doorbit"
version = "0.3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib:1.9.20"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
}

mavenPublishing {
    configure(KotlinJvm(
        javadocJar = JavadocJar.Dokka("dokkaHtml"),
        // whether to publish a sources jar
        sourcesJar = true,
    ))
    publishToMavenCentral(SonatypeHost.S01, true)
    signAllPublications()
}