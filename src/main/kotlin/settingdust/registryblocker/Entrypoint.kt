package settingdust.registryblocker

import com.mojang.serialization.JsonOps
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import org.apache.logging.log4j.LogManager
import org.quiltmc.qkl.library.serialization.CodecFactory

object RegistryBlocker {
    const val ID = "registry-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    val configPath =
        (FabricLoader.getInstance().configDir / "$ID.json").also {
            if (!it.exists()) it.writeText("{}")
        }

    val CONFIG_CODEC = codecFactory.create<Map<Identifier, Set<Identifier>>>()
    var config =
        CONFIG_CODEC.parse(JsonOps.INSTANCE, JsonHelper.deserialize(configPath.readText())).result()
            .orElseThrow()

    fun reload() {
        config = CONFIG_CODEC.parse(JsonOps.INSTANCE, JsonHelper.deserialize(configPath.readText()))
            .result().orElseThrow()
    }
}

private val codecFactory = CodecFactory {
    codecs { unnamed(Identifier.CODEC) }
}

fun init() {}

fun <T> MutableMap<T, RegistryEntry.Reference<T>>.removeIntrusiveValues(
    blocked: Map<RegistryKey<T>, T>
) {
    for ((_, value) in blocked) {
        remove(value)
    }
}
