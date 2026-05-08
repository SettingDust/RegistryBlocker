package settingdust.registry_blocker.v26_1.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryLookupAdapter as IRegistryLookupAdapter

class RegistryLookupAdapter : IRegistryLookupAdapter {
    override fun containsEntry(registryId: CommonIdentifier, entryId: CommonIdentifier): Boolean {
        val registry = BuiltInRegistries.REGISTRY.getValue(registryId as Identifier)
        return registry?.containsKey(entryId as Identifier) == true
    }
}