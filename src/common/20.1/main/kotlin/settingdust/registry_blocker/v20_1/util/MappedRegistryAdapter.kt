package settingdust.registry_blocker.v20_1.util

import com.mojang.serialization.Lifecycle
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import settingdust.registry_blocker.util.MappedRegistryAdapter as IMappedRegistryAdapter

internal data class MappedRegistryHandle20<T : Any>(
    val registry: MappedRegistry<T>
) : IMappedRegistryAdapter.MappedRegistryHandle<T>

class MappedRegistryAdapter : IMappedRegistryAdapter {
    override fun <T : Any> createMappedRegistry(registryKey: ResourceKey<Registry<T>>): IMappedRegistryAdapter.MappedRegistryHandle<T> {
        return MappedRegistryHandle20(MappedRegistry(registryKey, Lifecycle.stable(), false))
    }

    override fun <T : Any> registerEntry(
        mappedRegistry: IMappedRegistryAdapter.MappedRegistryHandle<T>,
        key: ResourceKey<T>,
        value: T
    ): Boolean {
        val registry = (mappedRegistry as MappedRegistryHandle20<T>).registry
        return registry.register(key, value, Lifecycle.stable()) != null
    }
}