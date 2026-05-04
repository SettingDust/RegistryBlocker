package settingdust.registry_blocker.util

import java.nio.file.Path

interface LoaderAdapter {
    companion object : LoaderAdapter by ServiceLoaderUtil.findService()

    val isClient: Boolean

    val gameDir: Path

    val configDir: Path get() = gameDir.resolve("config")

    fun isModLoaded(modId: String): Boolean
}