package settingdust.registry_blocker.forge

import dev.nyon.klf.MOD_BUS
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import settingdust.registry_blocker.RegistryBlocker
import settingdust.registry_blocker.util.ServerReloadCallback
import settingdust.registry_blocker.util.Entrypoint

@Mod(RegistryBlocker.ID)
object RegistryBlockerForge {
    init {
        requireNotNull(RegistryBlocker)
        Entrypoint.construct()
        MOD_BUS.apply {
            addListener<FMLCommonSetupEvent> {
                Entrypoint.init()
            }
            addListener<FMLClientSetupEvent> { Entrypoint.clientInit() }
        }
        MinecraftForge.EVENT_BUS.apply {
            addListener<AddReloadListenerEvent> {
                ServerReloadCallback.EVENT.invoker().onReload()
            }
        }
    }
}
