package settingdust.registry_blocker.util

import net.minecraft.resources.ResourceLocation

interface MinecraftAdapter {
    companion object : MinecraftAdapter by ServiceLoaderUtil.findService()

    fun id(namespace: String, path: String): ResourceLocation
}