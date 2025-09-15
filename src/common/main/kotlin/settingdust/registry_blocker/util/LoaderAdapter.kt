package settingdust.registry_blocker.util

import java.nio.file.Path

interface LoaderAdapter {
    companion object : LoaderAdapter by ServiceLoaderUtil.findService()

    val isClient: Boolean

    fun isModLoaded(modId: String): Boolean

    val configDir: Path

    fun onServerReload(callback: () -> Unit)
}
