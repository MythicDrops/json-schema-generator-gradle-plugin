package dev.mythicdrops.gradle.models

import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property

abstract class JsonSchemaDescriptor(private val name: String) : Named, java.io.Serializable {
    abstract val title: Property<String>
    abstract val sourceFiles: ConfigurableFileCollection

    override fun getName(): String = name

    fun sourceFile(file: Any) = sourceFiles.from(file)

    fun sourceFiles(vararg files: Any) = sourceFiles.from(files)

    override fun toString(): String {
        return "JsonSchemaDescriptor(name='$name', title=$title, sourceFiles=$sourceFiles)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JsonSchemaDescriptor) return false

        if (name != other.name) return false
        if (title != other.title) return false
        if (sourceFiles != other.sourceFiles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + sourceFiles.hashCode()
        return result
    }
}
