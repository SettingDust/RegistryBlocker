package settingdust.registry_blocker.v20_1.util

import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.IdentifierFactory
import settingdust.registry_blocker.util.MinecraftVersion
import settingdust.registry_blocker.util.SimpleIdentifier

class IdentifierFactory : IdentifierFactory {
    init {
        MinecraftVersion.V1201.requireCurrent()
    }

    override fun create(namespace: String, path: String): CommonIdentifier =
        SimpleIdentifier(namespace, path)

    override fun parse(value: String): CommonIdentifier {
        val native = ResourceLocation(value)
        return SimpleIdentifier(native.namespace, native.path)
    }
}
