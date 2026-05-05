package settingdust.registry_blocker.util

import com.mojang.serialization.Codec

interface IdentifierAdapter {
    companion object : IdentifierAdapter by ServiceLoaderUtil.findService()

    fun create(namespace: String, path: String): CommonIdentifier
    fun parse(value: String): CommonIdentifier
    val codec: Codec<CommonIdentifier>
}
