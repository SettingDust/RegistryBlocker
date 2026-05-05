package settingdust.registry_blocker.fabric

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.util.ServerReloadCallback

object RegistryBlockerFabric {
    init {
        RegistryBlocker
    }

    fun init() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register { _, _ ->
            ServerReloadCallback.EVENT.invoker().onReload()
        }
    }

    fun clientInit() {}
}
