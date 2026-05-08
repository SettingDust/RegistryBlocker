package settingdust.registry_blocker.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

/**
 * SPI adapter for version-specific MappedRegistry operations.
 */
interface MappedRegistryAdapter {
    companion object : MappedRegistryAdapter by ServiceLoaderUtil.findService()

    interface MappedRegistryHandle<T : Any>

    fun <T : Any> createMappedRegistry(registryKey: ResourceKey<Registry<T>>): MappedRegistryHandle<T>

    fun <T : Any> registerEntry(
        mappedRegistry: MappedRegistryHandle<T>,
        key: ResourceKey<T>,
        value: T
    ): Boolean
}