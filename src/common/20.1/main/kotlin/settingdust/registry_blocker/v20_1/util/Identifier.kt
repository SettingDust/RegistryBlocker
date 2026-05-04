package settingdust.registry_blocker.v20_1.util

import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.Identifier

fun Identifier.toNative(): ResourceLocation = ResourceLocation(namespace, path)

fun ResourceLocation.toCommon(): Identifier = Identifier(namespace, path)
