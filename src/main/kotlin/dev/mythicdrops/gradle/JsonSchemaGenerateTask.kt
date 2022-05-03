package dev.mythicdrops.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer
import com.saasquatch.jsonschemainferrer.SpecVersion
import dev.mythicdrops.gradle.models.InferredJsonSchemaDescriptor
import dev.mythicdrops.gradle.models.LoadedJsonSchemaDescriptor
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType

abstract class JsonSchemaGenerateTask : DefaultTask() {
    companion object {
        private val inferrer: JsonSchemaInferrer = JsonSchemaInferrer.newBuilder()
            .setSpecVersion(SpecVersion.DRAFT_07)
            .build()
    }

    @get:OutputDirectory
    abstract val destinationDirectory: DirectoryProperty

    private val jsonSchemaDescriptors =
        project.extensions.getByType<JsonSchemaExtension>().jsonSchemaDescriptors.toList()
    private val jsonMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()
    private val yamlMapper: ObjectMapper = YAMLMapper().findAndRegisterModules()

    init {
        description = "Generates JSON schemas from input files"
        inputs.files(jsonSchemaDescriptors.flatMap { descriptor -> descriptor.sourceFiles })
    }

    @TaskAction
    fun generateJsonSchemas() {
        val loadedJsonSchemaDescriptors = jsonSchemaDescriptors.map { descriptor ->
            LoadedJsonSchemaDescriptor(
                name = descriptor.name,
                title = descriptor.title.get(),
                sourceFileContents = descriptor.sourceFiles.map {
                    when (it.extension) {
                        "yml", "yaml" -> {
                            yamlMapper.readTree(it.readText())
                        }
                        "json" -> {
                            jsonMapper.readTree(it.readText())
                        }
                        else -> {
                            throw RuntimeException("Unsupported file extension: ${it.extension}")
                        }
                    }
                }
            )
        }
        val inferredJsonSchemaDescriptors = loadedJsonSchemaDescriptors.map {
            InferredJsonSchemaDescriptor(
                name = it.name,
                title = it.title,
                schema = inferrer.inferForSamples(it.sourceFileContents)
            )
        }
        inferredJsonSchemaDescriptors.forEach {
            jsonMapper.writeValue(destinationDirectory.file("${it.name}.json").get().asFile, it.schema)
        }
    }
}
