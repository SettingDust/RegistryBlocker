package settingdust.registryblocker

import net.minecraft.registry.RegistryKey

interface BlockingRegistry<T> {
    val `registryblocker$blocked`: MutableMap<RegistryKey<T>, T>
}
