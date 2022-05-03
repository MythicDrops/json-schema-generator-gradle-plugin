# json-schema-generator-gradle-plugin

> Gradle plugin to generate JSON schema files.

## Sample Usage

```kotlin
jsonSchema {
    jsonSchemaDescriptors {
        create("config") {
            title.set("config")
            sourceFile(file("mythicdrops-plugin/src/main/resources/config.yml"))
        }
    }
}
```
