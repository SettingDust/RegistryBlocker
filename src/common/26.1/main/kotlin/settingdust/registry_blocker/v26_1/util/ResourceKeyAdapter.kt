package settingdust.registry_blocker.v26_1.util

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.ResourceKeyAdapter as IResourceKeyAdapter

class ResourceKeyAdapter : IResourceKeyAdapter {
    override fun <T : Any> createRegistryKey(id: CommonIdentifier): ResourceKey<Registry<T>> {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(id.namespace, id.path))
    }

    override fun <T : Any> createEntryKey(
        registryKey: ResourceKey<Registry<T>>,
        id: CommonIdentifier
    ): ResourceKey<T> {
        return ResourceKey.create(registryKey, Identifier.fromNamespaceAndPath(id.namespace, id.path))
    }
}