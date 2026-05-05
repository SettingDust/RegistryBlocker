package settingdust.registry_blocker.v21_1.config

import me.fzzyhmstrs.fzzy_config.api.FileType
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedIdentifierMap
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
    var blockedRegistries: ValidatedIdentifierMap<List<ResourceLocation>> =
        ValidatedIdentifierMap(
            mapOf(),
            ValidatedIdentifier(),
            ValidatedIdentifier().toList()
        )

    override fun fileType(): FileType = FileType.JSON5

    @Suppress("UNCHECKED_CAST")
    override val blockedEntries: Map<CommonIdentifier, List<CommonIdentifier>>
        get() = blockedRegistries.get() as Map<CommonIdentifier, List<CommonIdentifier>>
}
