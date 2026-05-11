package settingdust.registry_blocker.fabric.util

import net.fabricmc.loader.api.FabricLoader
import settingdust.registry_blocker.util.MinecraftVersionNameProvider

class MinecraftVersionNameProvider : MinecraftVersionNameProvider {
    override fun currentVersionName(): String = FabricLoader.getInstance()
        .getModContainer("minecraft")
        .orElseThrow()
        .metadata
        .version
        .friendlyString
}
