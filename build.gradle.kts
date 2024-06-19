import groovy.lang.Closure

plugins {
    idea
    java
    `maven-publish`
    alias(catalog.plugins.idea.ext)

    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)

    alias(catalog.plugins.git.version)

    alias(catalog.plugins.fabric.loom)
}

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/gradle_issue_15754.gradle.kts")

group = "settingdust.registryblocker"

val gitVersion: Closure<String> by extra
version = gitVersion()

val id: String by rootProject.properties
val name: String by rootProject.properties
val author: String by rootProject.properties
val description: String by rootProject.properties

loom {
    mixin {
        defaultRefmapName = "$id.refmap.json"

        add("main", "$id.refmap.json")
    }

    accessWidenerPath = file("src/main/resources/$id.accesswidener")

    mods { register(id) { sourceSet(sourceSets["main"]) } }
}

dependencies {
    minecraft(catalog.minecraft.fabric)
    mappings(variantOf(catalog.mapping.yarn) { classifier("v2") })

    modImplementation(catalog.fabric.loader)
    modImplementation(catalog.fabric.api)
    modImplementation(catalog.fabric.kotlin)

    include(catalog.kinecraft.serialization)
    modImplementation(variantOf(catalog.kinecraft.serialization) { classifier("fabric") })
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = base.archivesName.get()
        }
    }

    repositories {
        maven("file://${rootProject.projectDir}/publish")
    }
}

val metadata =
    mapOf(
        "group" to group,
        "author" to author,
        "id" to id,
        "name" to name,
        "version" to version,
        "description" to description,
        "source" to "https://github.com/SettingDust/RegistryBlocker",
        "minecraft" to "~1.21",
        "fabric_loader" to ">=0.15",
        "fabric_kotlin" to ">=1.11",
        "modmenu" to "*",
    )

tasks {
    withType<ProcessResources> {
        inputs.properties(metadata)
        filesMatching(listOf("fabric.mod.json", "*.mixins.json")) { expand(metadata) }
    }
}
