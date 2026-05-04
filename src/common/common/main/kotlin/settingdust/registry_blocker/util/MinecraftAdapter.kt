package settingdust.registry_blocker.util

interface MinecraftAdapter {
    companion object : MinecraftAdapter by ServiceLoaderUtil.findService()
}