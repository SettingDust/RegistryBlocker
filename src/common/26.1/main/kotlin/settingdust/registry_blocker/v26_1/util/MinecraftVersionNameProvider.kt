package settingdust.registry_blocker.v26_1.util

import net.minecraft.SharedConstants
import settingdust.registry_blocker.util.MinecraftVersionNameProvider

class MinecraftVersionNameProvider : MinecraftVersionNameProvider {
    override fun currentVersionName(): String = SharedConstants.getCurrentVersion().name()
}
