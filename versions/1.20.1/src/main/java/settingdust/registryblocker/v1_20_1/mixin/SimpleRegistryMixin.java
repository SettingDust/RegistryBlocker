package settingdust.registryblocker.v1_20_1.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registryblocker.BlockingRegistry;
import settingdust.registryblocker.RegistryBlocker;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements BlockingRegistry<T> {
    @Shadow
    @Final
    RegistryKey<? extends Registry<T>> key;

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void registryblocker$filterRegistries(
        final RegistryKey<T> key,
        final T entry,
        final Lifecycle lifecycle,
        final CallbackInfoReturnable<RegistryEntry.Reference<T>> cir
    ) {
        if (RegistryBlocker.INSTANCE.getConfig().containsKey(this.key.getValue())
            && RegistryBlocker.INSTANCE.getConfig().get(key.getRegistry()).contains(key.getValue())) {
            getRegistryblocker$blocked().put(key, entry);
            RegistryBlocker.LOGGER.debug("[SimpleRegistry#add] Blocking registry entry: {}", key);
            cir.setReturnValue(null);
        }
    }
}
