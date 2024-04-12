package settingdust.registryblocker.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registryblocker.EntrypointKt;
import settingdust.registryblocker.RegistryBlocker;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void registryblocker$filterRegistries(
        final RegistryKey<?> key,
        final Object entry,
        final Lifecycle lifecycle,
        final CallbackInfoReturnable<RegistryEntry.Reference<?>> cir
    ) {
        if (EntrypointKt.getConfig().containsKey(key.getRegistry())
            && EntrypointKt.getConfig().get(key.getRegistry()).contains(key.getValue())) {
            RegistryBlocker.LOGGER.debug("[SimpleRegistry#add] Blocking registry entry: {}", key);
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getOrCreateEntry", at = @At("HEAD"))
    private void registryblocker$avoidReference(
        final RegistryKey<T> key,
        final CallbackInfoReturnable<RegistryEntry.Reference<T>> cir
    ) {
        if (EntrypointKt.getConfig().containsKey(key.getRegistry())
            && EntrypointKt.getConfig().get(key.getRegistry()).contains(key.getValue())) {
            throw new IllegalStateException("Avoid requesting a blocked registry entry " + key +
                                            ". You should use datapack override the file requiring this entry.");
        }
    }
}
