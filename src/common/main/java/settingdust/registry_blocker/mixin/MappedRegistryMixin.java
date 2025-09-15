package settingdust.registry_blocker.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registry_blocker.BlockingRegistry;
import settingdust.registry_blocker.RegistryBlockerKt;
import settingdust.registry_blocker.RegistryBlocker;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements BlockingRegistry<T> {

    @Shadow
    @Final
    ResourceKey<? extends Registry<T>> key;
    @Shadow
    @Nullable
    private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
    @Unique private final Map<ResourceKey<T>, T> registryblocker$blocked = new HashMap<>();

    @Override
    public @NotNull Map<ResourceKey<T>, T> getRegistryblocker$blocked() {
        return registryblocker$blocked;
    }

    @Inject(method = "getOrCreateHolderOrThrow", at = @At("HEAD"))
    private void registryblocker$avoidReference(
        final ResourceKey<T> key,
        final CallbackInfoReturnable<Holder.Reference<T>> cir
    ) {
        if (registryblocker$blocked.containsKey(key)) {
            throw new IllegalStateException("Avoid requesting a blocked registry entry " + key +
                                            ". You should use datapack override the file requiring this entry.");
        }
    }

    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/Map;isEmpty()Z"))
    private void registryblocker$removeValue(
        final CallbackInfoReturnable<Registry<T>> cir
    ) {
        if (unregisteredIntrusiveHolders != null) {
            RegistryBlockerKt.removeIntrusiveValues(unregisteredIntrusiveHolders, registryblocker$blocked);
            RegistryBlocker.LOGGER.debug("Removed blocked intrusive registry entries for {}.", key);
        }
    }
}
