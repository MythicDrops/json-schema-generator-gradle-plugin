package dev.mythicdrops.gradle.models

import com.fasterxml.jackson.databind.JsonNode

data class InferredJsonSchemaDescriptor(
    val name: String,
    val title: String,
    val schema: JsonNode
)
