package settingdust.registry_blocker.neoforge.util

import net.neoforged.fml.loading.LoadingModList
import settingdust.registry_blocker.util.MinecraftVersionNameProvider

class MinecraftVersionNameProvider : MinecraftVersionNameProvider {
    override fun currentVersionName(): String = LoadingModList.get()
        .getModFileById("minecraft")
    .versionString()
}
