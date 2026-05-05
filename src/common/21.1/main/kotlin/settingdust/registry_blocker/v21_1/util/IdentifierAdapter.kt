package settingdust.registry_blocker.v21_1.util

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.IdentifierAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class IdentifierAdapter : IdentifierAdapter {
    init {
        MinecraftVersion.V1211.requireCurrent()
    }

    override fun create(namespace: String, path: String): CommonIdentifier =
        ResourceLocation.fromNamespaceAndPath(namespace, path) as CommonIdentifier

    override fun parse(value: String): CommonIdentifier =
        ResourceLocation.parse(value) as CommonIdentifier

    @Suppress("UNCHECKED_CAST")
    override val codec: Codec<CommonIdentifier>
        get() = ResourceLocation.CODEC as Codec<CommonIdentifier>
}
