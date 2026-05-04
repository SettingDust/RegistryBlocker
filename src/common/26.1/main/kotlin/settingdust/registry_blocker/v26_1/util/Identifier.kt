package settingdust.registry_blocker.v26_1.util

import net.minecraft.resources.Identifier as MinecraftIdentifier
import settingdust.registry_blocker.util.Identifier

fun Identifier.toNative(): MinecraftIdentifier = MinecraftIdentifier.fromNamespaceAndPath(namespace, path)

fun MinecraftIdentifier.toCommon(): Identifier = Identifier(namespace, path)
