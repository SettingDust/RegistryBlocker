package settingdust.registry_blocker.v20_1.util

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.IdentifierAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class IdentifierAdapter : IdentifierAdapter {
    init {
        MinecraftVersion.V1201.requireCurrent()
    }

    override fun create(namespace: String, path: String): CommonIdentifier =
        ResourceLocation(namespace, path) as CommonIdentifier

    override fun parse(value: String): CommonIdentifier =
        ResourceLocation(value) as CommonIdentifier

    @Suppress("UNCHECKED_CAST")
    override val codec: Codec<CommonIdentifier>
        get() = ResourceLocation.CODEC as Codec<CommonIdentifier>
}
