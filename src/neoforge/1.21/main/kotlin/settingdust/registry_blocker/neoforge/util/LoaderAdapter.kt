package settingdust.registry_blocker.neoforge.util

import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.fml.loading.LoadingModList
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient = FMLLoader.getDist().isClient

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null

    override val configDir = FMLPaths.CONFIGDIR.get()

    override fun onServerReload(callback: () -> Unit) {
        NeoForge.EVENT_BUS.addListener<AddReloadListenerEvent> { callback() }
    }
}
