package settingdust.registry_blocker.neoforge.util

import net.neoforged.api.distmarker.Dist
import settingdust.registry_blocker.util.ServiceLoaderUtil

interface NeoForgeAdapter {
    companion object : NeoForgeAdapter by ServiceLoaderUtil.findService()

    val dist: Dist
}
