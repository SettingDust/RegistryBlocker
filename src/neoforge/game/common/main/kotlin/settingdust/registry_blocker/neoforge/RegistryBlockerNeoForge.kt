package settingdust.registry_blocker.neoforge

import dev.nyon.klf.MOD_BUS
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.util.Entrypoint
import settingdust.registry_blocker.util.ServerReloadCallback

@Mod(RegistryBlocker.ID)
object RegistryBlockerNeoForge {
    init {
        requireNotNull(RegistryBlocker)
        Entrypoint.construct()
        MOD_BUS.apply {
            addListener<FMLCommonSetupEvent> {
                Entrypoint.init()
            }
            addListener<FMLClientSetupEvent> { Entrypoint.clientInit() }
        }
    }
}
