package settingdust.registry_blocker.neoforge.v21_1.util

import net.neoforged.fml.loading.FMLLoader
import settingdust.registry_blocker.neoforge.util.NeoForgeAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class NeoForgeAdapter : NeoForgeAdapter {
    init {
        MinecraftVersion.V1211.requireCurrent()
    }

    override val dist = FMLLoader.getDist()!!
}
