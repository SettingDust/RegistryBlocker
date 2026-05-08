package settingdust.registry_blocker.v21_1.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registry_blocker.BlockingRegistry;
import settingdust.registry_blocker.RegistryBlocker;
import settingdust.registry_blocker.RegistryBlockerKt;
import settingdust.registry_blocker.util.RegistryBlockerConfig;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements BlockingRegistry<T> {
    @Shadow
    @Nullable
    private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;

    @Shadow
    public abstract ResourceKey<? extends Registry<T>> key();

    @Unique
    private final @NotNull Map<@NotNull ResourceKey<T>, T> registryblocker$blocked = new HashMap<>();

    @Override
    public @NotNull Map<@NotNull ResourceKey<T>, T> getRegistryblocker$blocked() {
        return registryblocker$blocked;
    }

    @WrapMethod(method = "register")
    private Holder.Reference<T> registryblocker$filterRegistries(
            final ResourceKey<T> key,
            final T entry,
            final RegistrationInfo info,
            final Operation<Holder.Reference<T>> original
    ) {
        if (RegistryBlockerConfig.Companion.getBlockedEntries().containsKey(key.registry())
                && RegistryBlockerConfig.Companion.getBlockedEntries().get(key.registry()).contains(key.location())) {
            if (this instanceof DefaultedRegistry<?> defaulted && key.location().equals(defaulted.getDefaultKey())) {
                RegistryBlocker.LOGGER.warn(
                        "[MappedRegistry#register] Cannot block default registry entry {} in {}. Skipping.",
                        key.location(), key.registry()
                );
                return original.call(key, entry, info);
            }

            getRegistryblocker$blocked().put(key, entry);
            RegistryBlocker.LOGGER.debug("[MappedRegistry#register] Blocking registry entry: {}", key);
            return null;
        }
        return original.call(key, entry, info);
    }

    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/Map;isEmpty()Z"))
    private void registryblocker$removeValue(final CallbackInfoReturnable<Registry<T>> cir) {
        if (unregisteredIntrusiveHolders != null) {
            RegistryBlockerKt.removeIntrusiveValues(unregisteredIntrusiveHolders, registryblocker$blocked);
            RegistryBlocker.LOGGER.debug("Removed blocked intrusive registry entries for {}.", key());
        }
    }

    @WrapMethod(method = "getKey")
    private ResourceLocation registryblocker$getKeyWithFallback(
            final T entry,
            final Operation<ResourceLocation> original
    ) {
        var result = original.call(entry);
        if (result == null) {
            for (final var blocked : getRegistryblocker$blocked().entrySet()) {
                if (blocked.getValue() == entry) {
                    RegistryBlocker.LOGGER.debug(
                            "[MappedRegistry#getKey] Fallback to blocked entry for: {}",
                            blocked.getKey()
                    );
                    return blocked.getKey().location();
                }
            }
        }
        return result;
    }
}