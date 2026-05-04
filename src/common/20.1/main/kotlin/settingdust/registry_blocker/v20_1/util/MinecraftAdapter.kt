package settingdust.registry_blocker.v20_1.util

import settingdust.registry_blocker.util.MinecraftAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class MinecraftAdapter : MinecraftAdapter {
    init {
        MinecraftVersion.V1201.requireCurrent()
    }
}