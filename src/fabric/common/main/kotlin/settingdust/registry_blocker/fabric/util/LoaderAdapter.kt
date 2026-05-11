package settingdust.registry_blocker.fabric.util

import java.nio.file.Path
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import settingdust.registry_blocker.util.LoaderAdapter

class LoaderAdapter : LoaderAdapter {
    override val isClient = FabricLoader.getInstance().environmentType === EnvType.CLIENT

    override val isGameTest = System.getProperty("fabric-api.gametest") != null

    override val gameDir: Path = FabricLoader.getInstance().gameDir

    override val configDir: Path = FabricLoader.getInstance().configDir

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)
}