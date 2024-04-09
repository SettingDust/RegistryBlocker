@file:OptIn(ExperimentalSerializationApi::class)

package settingdust.registryblocker

import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import settingdust.kinecraft.serialization.ResourceLocationStringSerializer

object RegistryBlocker {
    const val ID = "registry-blocker"
}

private val json = Json {
    serializersModule = SerializersModule { contextual(ResourceLocationStringSerializer) }
}

private val configPath =
    (FabricLoader.getInstance().configDir / "${RegistryBlocker.ID}.json").also {
        if (!it.exists()) it.writeText("{}")
    }

var config = json.decodeFromStream<Map<Identifier, Set<Identifier>>>(configPath.inputStream())

fun init() {
    ServerLifecycleEvents.START_DATA_PACK_RELOAD.register { _, _ ->
        config = json.decodeFromStream(configPath.inputStream())
    }
}
