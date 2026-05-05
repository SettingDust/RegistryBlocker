package settingdust.registry_blocker

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import org.apache.logging.log4j.LogManager
import settingdust.registry_blocker.util.IdentifierAdapter
import settingdust.registry_blocker.util.RegistryBlockerConfig

object RegistryBlocker {
    const val ID = "registry_blocker"

    @JvmField
    val LOGGER = LogManager.getLogger()

    init {
        requireNotNull(RegistryBlockerConfig)
    }

    fun id(path: String) = IdentifierAdapter.create(ID, path)
}

fun <T : Any> MutableMap<T, Holder.Reference<T>>.removeIntrusiveValues(blocked: Map<ResourceKey<T>, T>) {
    for ((_, value) in blocked) {
        remove(value)
    }
}
