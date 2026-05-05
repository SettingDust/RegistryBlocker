package settingdust.registry_blocker.util

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.config.Config

interface RegistryBlockerConfig {
    companion object :
        RegistryBlockerConfig by ConfigApi.registerAndLoadConfig({ ServiceLoaderUtil.findService<RegistryBlockerConfig>() as Config }) as RegistryBlockerConfig

    val blockedEntries: Map<CommonIdentifier, List<CommonIdentifier>>
}
