package settingdust.registry_blocker.v21_1.config

import me.fzzyhmstrs.fzzy_config.api.FileType
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.mixin.BuiltInRegistriesAccessor
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryBlockerConfig as IRegistryBlockerConfig
import settingdust.registry_blocker.util.ValidatedDynamicMap

class RegistryBlockerConfig : Config(
    ResourceLocation.fromNamespaceAndPath(RegistryBlocker.ID, RegistryBlocker.ID),
    folder = "",
    name = RegistryBlocker.ID,
), IRegistryBlockerConfig {
    @Suppress("UNCHECKED_CAST")
    override var blockedEntries =
        ValidatedDynamicMap(
            hashMapOf(),
            ValidatedIdentifier.ofRegistry(
                ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "item",
                ),
                BuiltInRegistriesAccessor.getRootRegistry(),
            ),
        ) { key ->
            val registry = BuiltInRegistries.REGISTRY.get(key)
            if (registry != null) {
                ValidatedIdentifier(
                    ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
                    AllowableIdentifiers(
                        { true },
                        { registry.keySet().toList() },
                        true,
                    ),
                ).toList()
            } else {
                ValidatedIdentifier().toList()
            }
        } as ValidatedDynamicMap<CommonIdentifier, List<CommonIdentifier>>

    override fun fileType(): FileType = FileType.JSON5
}
