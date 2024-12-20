package settingdust.registryblocker.v1_20_1.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import settingdust.registryblocker.BlockingRegistry;
import settingdust.registryblocker.RegistryBlocker;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements BlockingRegistry<T> {
    @Shadow
    @Final
    RegistryKey<? extends Registry<T>> key;

    @WrapMethod(method = "add")
    private RegistryEntry.Reference<T> registryblocker$filterRegistries(
        final RegistryKey<T> key,
        final T entry,
        final Lifecycle lifecycle,
        final Operation<RegistryEntry.Reference<T>> original
    ) {
        if (RegistryBlocker.INSTANCE.getConfig().containsKey(this.key.getValue())
            && RegistryBlocker.INSTANCE.getConfig().get(key.getRegistry()).contains(key.getValue())) {
            getRegistryblocker$blocked().put(key, entry);
            RegistryBlocker.LOGGER.debug("[SimpleRegistry#add] Blocking registry entry: {}", key);
            return null;
        }
        return original.call(key, entry, lifecycle);
    }
}
