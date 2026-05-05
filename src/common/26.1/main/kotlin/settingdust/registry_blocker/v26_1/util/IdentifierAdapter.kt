package settingdust.registry_blocker.v26_1.util

import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.IdentifierAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class IdentifierAdapter : IdentifierAdapter {
    init {
        MinecraftVersion.V261.requireCurrent()
    }

    override fun create(namespace: String, path: String): CommonIdentifier =
        Identifier.fromNamespaceAndPath(namespace, path) as CommonIdentifier

    override fun parse(value: String): CommonIdentifier =
        Identifier.parse(value) as CommonIdentifier

    @Suppress("UNCHECKED_CAST")
    override val codec: Codec<CommonIdentifier>
        get() = Identifier.CODEC as Codec<CommonIdentifier>
}
