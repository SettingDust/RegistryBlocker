package settingdust.registry_blocker.v21_1.config

import me.fzzyhmstrs.fzzy_config.api.FileType
import me.fzzyhmstrs.fzzy_config.config.Config
import net.minecraft.core.registries.BuiltInRegistries
import settingdust.registry_blocker.util.ValidatedDynamicMap
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier
import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryBlockerConfig as IRegistryBlockerConfig

class RegistryBlockerConfig : Config(
    ResourceLocation.fromNamespaceAndPath(RegistryBlocker.ID, RegistryBlocker.ID),
    folder = "",
    name = RegistryBlocker.ID
), IRegistryBlockerConfig {
    var blockedRegistries: ValidatedDynamicMap<ResourceLocation, List<ResourceLocation>> =
        ValidatedDynamicMap(
            mapOf(),
            ValidatedIdentifier.ofRegistry(ResourceLocation.fromNamespaceAndPath("minecraft", "item"), BuiltInRegistries.REGISTRY),
            { key ->
                val registry = BuiltInRegistries.REGISTRY.get(key)
                if (registry != null) ValidatedIdentifier.ofRegistry(ResourceLocation.fromNamespaceAndPath("minecraft", "air"), registry).toList()
                else ValidatedIdentifier().toList()
            }
        )

    override fun fileType(): FileType = FileType.JSON5

    @Suppress("UNCHECKED_CAST")
    override val blockedEntries: Map<CommonIdentifier, List<CommonIdentifier>>
        get() = blockedRegistries.get() as Map<CommonIdentifier, List<CommonIdentifier>>
}
