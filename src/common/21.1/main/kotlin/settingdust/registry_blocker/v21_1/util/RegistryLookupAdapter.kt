package settingdust.registry_blocker.v21_1.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryLookupAdapter as IRegistryLookupAdapter

class RegistryLookupAdapter : IRegistryLookupAdapter {
    override fun containsEntry(registryId: CommonIdentifier, entryId: CommonIdentifier): Boolean {
        val registry = BuiltInRegistries.REGISTRY.get(registryId as ResourceLocation)
        return registry?.containsKey(entryId as ResourceLocation) == true
    }
}