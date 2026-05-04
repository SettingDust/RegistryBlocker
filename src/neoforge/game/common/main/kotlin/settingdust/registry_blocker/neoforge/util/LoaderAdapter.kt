package settingdust.registry_blocker.neoforge.util

import java.nio.file.Path
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.fml.loading.LoadingModList
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient: Boolean
        get() = NeoForgeAdapter.dist.isClient

    override val gameDir: Path = FMLPaths.GAMEDIR.get()

    override val configDir: Path = FMLPaths.CONFIGDIR.get()

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null
}
