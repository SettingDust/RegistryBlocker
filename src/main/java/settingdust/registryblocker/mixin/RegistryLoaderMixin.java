package settingdust.registryblocker.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.registryblocker.RegistryBlocker;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @Inject(method = "parseAndAdd", at = @At("HEAD"), cancellable = true)
    private static <E> void registryblocker$filterBlocked(
        final MutableRegistry<E> registry,
        final Decoder<E> decoder,
        final RegistryOps<JsonElement> ops,
        final RegistryKey<E> key,
        final Resource resource,
        final RegistryEntryInfo entryInfo,
        final CallbackInfo ci
    ) {
        if (!RegistryBlocker.INSTANCE.getConfig().containsKey(key.getRegistry())) return;
        var blacklist = RegistryBlocker.INSTANCE.getConfig().get(key.getRegistry());
        if (!blacklist.contains(key.getValue())) return;
        RegistryBlocker.LOGGER.debug("[RegistryLoader] Blocking registry entry: {}", key);
        ci.cancel();
    }
}
