package settingdust.registry_blocker.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

/**
 * SPI adapter for version-specific ResourceKey creation.
 */
interface ResourceKeyAdapter {
    companion object : ResourceKeyAdapter by ServiceLoaderUtil.findService()

    fun <T : Any> createRegistryKey(id: CommonIdentifier): ResourceKey<Registry<T>>

    fun <T : Any> createEntryKey(registryKey: ResourceKey<Registry<T>>, id: CommonIdentifier): ResourceKey<T>
}