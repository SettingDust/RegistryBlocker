package settingdust.registry_blocker.v26_1.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import settingdust.registry_blocker.RegistryBlocker;
import settingdust.registry_blocker.util.RegistryBlockerConfig;

import java.util.stream.Stream;

@Mixin(RegistryLoadTask.class)
public abstract class RegistryLoadTaskMixin<T> {
    @Shadow
    protected abstract ResourceKey<? extends Registry<T>> registryKey();

    @ModifyVariable(
        method = "registerElements",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Stream<?> registryblocker$filterBlocked(
        Stream<?> original
    ) {
        var registryId = registryKey().identifier();
        var blacklist = RegistryBlockerConfig.Companion.getBlockedEntries().get(registryId);
        if (blacklist == null) return original;

        return original.filter(pending -> {
            var entryKey = ((PendingRegistrationAccessor) pending).registryblocker$getKey();
            var blocked = blacklist.contains(entryKey.identifier());
            if (blocked) {
                RegistryBlocker.LOGGER.debug(
                    "[RegistryLoadTask] Blocking registry entry: {} {}",
                    registryId,
                    entryKey.identifier()
                );
            }
            return !blocked;
        });
    }
}