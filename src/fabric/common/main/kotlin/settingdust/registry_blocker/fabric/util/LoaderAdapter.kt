package settingdust.registry_blocker.fabric.util

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient = FabricLoader.getInstance().environmentType === EnvType.CLIENT

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)
    override val configDir = FabricLoader.getInstance().configDir

    override fun onServerReload(callback: () -> Unit) {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register { _, _ -> callback() }
    }
}
