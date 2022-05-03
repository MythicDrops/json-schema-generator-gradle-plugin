package dev.mythicdrops.gradle.models

import com.fasterxml.jackson.databind.JsonNode

data class LoadedJsonSchemaDescriptor(
    val name: String,
    val title: String,
    val sourceFileContents: List<JsonNode>
)
