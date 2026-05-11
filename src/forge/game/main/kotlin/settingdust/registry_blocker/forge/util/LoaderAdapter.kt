package settingdust.registry_blocker.forge.util

import java.nio.file.Path
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.fml.loading.LoadingModList
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient: Boolean
        get() = FMLLoader.getDist().isClient

    override val isGameTest = System.getProperty("forge.enabledGameTestNamespaces") != null ||
        System.getProperties().stringPropertyNames().any {
            it.equals("forge.enabledGameTestNamespaces", ignoreCase = true)
        }

    override val gameDir: Path = FMLPaths.GAMEDIR.get()

    override val configDir: Path = FMLPaths.CONFIGDIR.get()

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null
}