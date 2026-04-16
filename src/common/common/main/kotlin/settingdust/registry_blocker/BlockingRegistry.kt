package settingdust.registry_blocker

import net.minecraft.resources.ResourceKey

interface BlockingRegistry<T> {
    val `registryblocker$blocked`: MutableMap<ResourceKey<T>, T>
}
