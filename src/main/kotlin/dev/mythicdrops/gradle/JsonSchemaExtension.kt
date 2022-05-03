package dev.mythicdrops.gradle

import dev.mythicdrops.gradle.models.JsonSchemaDescriptor
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty

/**
 * Configuration for the plugin.
 *
 * @property destinationDirectory where should schema files be written
 * @property jsonSchemaDescriptors known JSON Schema descriptors
 */
abstract class JsonSchemaExtension {
    abstract val destinationDirectory: DirectoryProperty

    abstract val jsonSchemaDescriptors: NamedDomainObjectContainer<JsonSchemaDescriptor>

    fun jsonSchemaDescriptors(action: Action<in NamedDomainObjectContainer<JsonSchemaDescriptor>>) {
        action.execute(jsonSchemaDescriptors)
    }
}
