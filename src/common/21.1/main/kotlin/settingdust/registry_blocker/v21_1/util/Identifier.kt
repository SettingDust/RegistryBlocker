package settingdust.registry_blocker.v21_1.util

import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.Identifier

fun Identifier.toNative(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

fun ResourceLocation.toCommon(): Identifier = Identifier(namespace, path)
