package settingdust.registry_blocker.v26_1.config

import me.fzzyhmstrs.fzzy_config.api.FileType
import me.fzzyhmstrs.fzzy_config.config.Config
import net.minecraft.core.registries.BuiltInRegistries
import settingdust.registry_blocker.util.ValidatedDynamicMap
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier
import net.minecraft.resources.Identifier
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryBlockerConfig as IRegistryBlockerConfig

class RegistryBlockerConfig : Config(
    Identifier.fromNamespaceAndPath(RegistryBlocker.ID, RegistryBlocker.ID),
    folder = "",
    name = RegistryBlocker.ID
), IRegistryBlockerConfig {
    var blockedRegistries: ValidatedDynamicMap<Identifier, List<Identifier>> =
        ValidatedDynamicMap(
            mapOf(),
            ValidatedIdentifier.ofRegistry(Identifier.fromNamespaceAndPath("minecraft", "item"), BuiltInRegistries.REGISTRY),
            { key ->
                val registry = BuiltInRegistries.REGISTRY.getValue(key)
                if (registry != null) ValidatedIdentifier.ofRegistry(Identifier.fromNamespaceAndPath("minecraft", "air"), registry).toList()
                else ValidatedIdentifier().toList()
            }
        )

    override fun fileType(): FileType = FileType.JSON5

    @Suppress("UNCHECKED_CAST")
    override val blockedEntries: Map<CommonIdentifier, List<CommonIdentifier>>
        get() = blockedRegistries.get() as Map<CommonIdentifier, List<CommonIdentifier>>
}
