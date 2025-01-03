package settingdust.registryblocker.mixin;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registryblocker.BlockingRegistry;
import settingdust.registryblocker.EntrypointKt;
import settingdust.registryblocker.RegistryBlocker;

import java.util.HashMap;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements BlockingRegistry<T> {
    @Shadow
    @Final
    RegistryKey<? extends Registry<T>> key;

    @Shadow
    @Nullable
    private Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;

    @Unique private final Map<RegistryKey<T>, T> registryblocker$blocked = new HashMap<>();

    @Override
    public @NotNull Map<RegistryKey<T>, T> getRegistryblocker$blocked() {
        return registryblocker$blocked;
    }

    @Inject(method = "getOrCreateEntry", at = @At("HEAD"))
    private void registryblocker$avoidReference(
        final RegistryKey<T> key,
        final CallbackInfoReturnable<RegistryEntry.Reference<T>> cir
    ) {
        if (registryblocker$blocked.containsKey(key)) {
            throw new IllegalStateException("Avoid requesting a blocked registry entry " + key +
                                            ". You should use datapack override the file requiring this entry.");
        }
    }

    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/Map;isEmpty()Z"))
    private void registryblocker$avoidReference(
        final CallbackInfoReturnable<Registry<T>> cir
    ) {
        if (intrusiveValueToEntry != null) {
            EntrypointKt.removeIntrusiveValues(intrusiveValueToEntry, registryblocker$blocked);
            RegistryBlocker.LOGGER.debug("Removing a blocked intrusive registry entry {}.", key);
        }
    }
}
