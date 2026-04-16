package settingdust.registry_blocker.forge.util

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.fml.loading.LoadingModList
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient = FMLLoader.getDist().isClient

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null

    override val configDir = FMLPaths.CONFIGDIR.get()

    override fun onServerReload(callback: () -> Unit) {
        MinecraftForge.EVENT_BUS.addListener<AddReloadListenerEvent> { callback() }
    }
}
