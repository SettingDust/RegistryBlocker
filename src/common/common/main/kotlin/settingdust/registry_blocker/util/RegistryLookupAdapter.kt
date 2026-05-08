package settingdust.registry_blocker.util

/**
 * SPI adapter for querying built-in registry contents across Minecraft versions.
 */
interface RegistryLookupAdapter {
    companion object : RegistryLookupAdapter by ServiceLoaderUtil.findService()

    fun containsEntry(registryId: CommonIdentifier, entryId: CommonIdentifier): Boolean
}