package settingdust.registry_blocker

import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writeText
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import org.apache.logging.log4j.LogManager
import settingdust.registry_blocker.util.LoaderAdapter
import settingdust.registry_blocker.util.ServiceLoaderUtil

object RegistryBlocker {
    const val ID = "registry-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    val configPath by lazy {
        (LoaderAdapter.configDir / "$ID.json").also {
            if (!it.exists()) it.writeText("{}")
        }
    }

    val CONFIG_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC.listOf())
    var config: Map<ResourceLocation, List<ResourceLocation>>

    init {
        ServiceLoaderUtil.defaultLogger = LOGGER
        config =
            CONFIG_CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(configPath.reader())).result()
                .orElseThrow()
        LoaderAdapter.onServerReload {
            config = CONFIG_CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(configPath.reader()))
                .result().orElseThrow()
        }
    }
}

fun <T> MutableMap<T, Holder.Reference<T>>.removeIntrusiveValues(blocked: Map<ResourceKey<T>, T>) {
    for ((_, value) in blocked) {
        remove(value)
    }
}
