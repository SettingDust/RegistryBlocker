package settingdust.registry_blocker.neoforge.v21_1

import dev.nyon.klf.MOD_BUS
import settingdust.registry_blocker.util.Entrypoint
import settingdust.registry_blocker.util.MinecraftVersion

class RegistryBlockerNeoForgeEntrypoint : Entrypoint {
    init {
        MinecraftVersion.V1211.requireCurrent()
        @Suppress("RedundantRequireNotNullCall")
        requireNotNull(MOD_BUS)
    }

    override fun construct() {
    }
}
