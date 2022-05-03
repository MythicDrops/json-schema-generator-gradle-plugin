
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    id("com.gradle.plugin-publish")
    id("org.shipkit.shipkit-auto-version")
    id("org.shipkit.shipkit-changelog")
    id("org.shipkit.shipkit-github-release")
    id("com.adarshr.test-logger")
    id("com.github.johnrengelman.shadow")
}

group = "dev.mythicdrops"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withJavadocJar()
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("jsonSchemaGenerator") {
            id = "dev.mythicdrops.gradle.json-schema"
            displayName = "jsonSchemaGenerator"
            description = "Generator for JSON schema files."
            implementationClass = "dev.mythicdrops.gradle.JsonSchemaGeneratorPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/MythicDrops/json-schema-generator-gradle-plugin"
    vcsUrl = "https://github.com/MythicDrops/json-schema-generator-gradle-plugin"
    tags = listOf("json", "schema", "json-schema", "generator")
}

tasks {
    // get dokkaJavadoc task and make javadocJar depend on it
    val dokkaJavadoc by this
    getByName<Jar>("javadocJar") {
        dependsOn(dokkaJavadoc)
        from(dokkaJavadoc)
    }

    // compile targeting JDK8
    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
    }

    // use JUnit Jupiter
    withType<Test>() {
        useJUnitPlatform()
    }

    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>() {
        archiveClassifier.set("")
    }
}

repositories {
    gradlePluginPortal() // other plugins on the plugin portal
    mavenCentral() // general dependencies
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:_"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.saasquatch:json-schema-inferrer:_")
}

val generateChangelog = tasks.getByName<org.shipkit.changelog.GenerateChangelogTask>("generateChangelog") {
    previousRevision = project.ext.get("shipkit-auto-version.previous-tag")?.toString()
    githubToken = System.getenv("GITHUB_TOKEN")
    repository = "MythicDrops/json-schema-generator"
}

tasks.getByName<org.shipkit.github.release.GithubReleaseTask>("githubRelease") {
    dependsOn(generateChangelog)
    repository = generateChangelog.repository
    changelog = generateChangelog.outputFile
    githubToken = System.getenv("GITHUB_TOKEN")
    newTagRevision = System.getenv("GITHUB_SHA")
}

project.ext.set("gradle.publish.key", System.getenv("GRADLE_PUBLISH_KEY"))
project.ext.set("gradle.publish.secret", System.getenv("GRADLE_PUBLISH_SECRET"))
