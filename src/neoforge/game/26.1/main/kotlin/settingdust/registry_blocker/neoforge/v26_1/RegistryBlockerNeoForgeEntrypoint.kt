package settingdust.registry_blocker.neoforge.v26_1

import dev.nyon.klf.MOD_BUS
import settingdust.registry_blocker.util.Entrypoint
import settingdust.registry_blocker.util.MinecraftVersion

class RegistryBlockerNeoForgeEntrypoint : Entrypoint {
    init {
        MinecraftVersion.V261.requireCurrent()
        @Suppress("RedundantRequireNotNullCall")
        requireNotNull(MOD_BUS)
    }

    override fun construct() {
    }
}
