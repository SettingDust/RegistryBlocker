package settingdust.registry_blocker.neoforge.v21_1

import dev.nyon.klf.MOD_BUS
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
import settingdust.registry_blocker.util.Entrypoint
import settingdust.registry_blocker.util.MinecraftVersion
import settingdust.registry_blocker.util.ServerReloadCallback

class RegistryBlockerNeoForgeEntrypoint : Entrypoint {
    init {
        MinecraftVersion.V1211.requireCurrent()
        @Suppress("RedundantRequireNotNullCall")
        requireNotNull(MOD_BUS)
    }

    override fun init() {
        NeoForge.EVENT_BUS.apply {
            addListener<AddReloadListenerEvent> {
                ServerReloadCallback.EVENT.invoker().onReload()
            }
        }
    }
}
