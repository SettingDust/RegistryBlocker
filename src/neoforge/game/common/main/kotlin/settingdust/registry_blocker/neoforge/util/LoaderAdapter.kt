package settingdust.registry_blocker.neoforge.util

import net.neoforged.fml.loading.LoadingModList
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient: Boolean
        get() = NeoForgeAdapter.dist.isClient

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null
}
