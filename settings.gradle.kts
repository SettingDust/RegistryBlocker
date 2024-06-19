extra["minecraft"] = "1.21"

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/common.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/kotlin.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/fabric.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/neoforge.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/modmenu.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/vanillagradle.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/mixin.gradle.kts")

dependencyResolutionManagement.versionCatalogs.named("catalog") {}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

val name: String by settings

rootProject.name = name
