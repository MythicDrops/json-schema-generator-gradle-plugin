package dev.mythicdrops.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

open class JsonSchemaGeneratorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<JsonSchemaExtension>("jsonSchema").apply {
            destinationDirectory.convention(target.layout.buildDirectory.dir("generated-sources/json-schema"))
        }

        target.tasks.register<JsonSchemaGenerateTask>("generateJsonSchema") {
            destinationDirectory.set(extension.destinationDirectory)
        }
    }
}
