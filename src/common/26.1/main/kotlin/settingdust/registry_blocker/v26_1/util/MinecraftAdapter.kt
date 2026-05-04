package settingdust.registry_blocker.v26_1.util

import settingdust.registry_blocker.util.MinecraftAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class MinecraftAdapter : MinecraftAdapter {
    init {
        MinecraftVersion.V261.requireCurrent()
    }
}
