package settingdust.registry_blocker.neoforge.v26_1

import dev.nyon.klf.MOD_BUS
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddServerReloadListenersEvent
import settingdust.registry_blocker.util.Entrypoint
import settingdust.registry_blocker.util.MinecraftVersion
import settingdust.registry_blocker.util.ServerReloadCallback

class RegistryBlockerNeoForgeEntrypoint : Entrypoint {
    init {
        MinecraftVersion.V261.requireCurrent()
        @Suppress("RedundantRequireNotNullCall")
        requireNotNull(MOD_BUS)
    }

    override fun init() {
        NeoForge.EVENT_BUS.apply {
            addListener<AddServerReloadListenersEvent> {
                ServerReloadCallback.EVENT.invoker().onReload()
            }
        }
    }
}
