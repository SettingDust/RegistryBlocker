package settingdust.registry_blocker.v26_1.util

import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.MinecraftAdapter

class MinecraftAdapter : MinecraftAdapter {
    override fun id(namespace: String, path: String) = ResourceLocation.fromNamespaceAndPath(namespace, path)
}
