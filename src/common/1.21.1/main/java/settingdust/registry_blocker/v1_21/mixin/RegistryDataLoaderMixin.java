package settingdust.registry_blocker.v1_21.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.registry_blocker.RegistryBlocker;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Inject(
        method = "loadElementFromResource",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/Resource;openAsReader()Ljava/io/BufferedReader;"
        )
    )
    private static <E> void registryblocker$filterBlocked(
        final WritableRegistry<E> par1,
        final Decoder<E> par2,
        final RegistryOps<JsonElement> par3,
        final ResourceKey<E> key,
        final Resource par5,
        final RegistrationInfo par6,
        final CallbackInfo ci
    ) {
        if (!RegistryBlocker.INSTANCE.getConfig().containsKey(key.registry())) return;
        var blacklist = RegistryBlocker.INSTANCE.getConfig().get(key.registry());
        if (!blacklist.contains(key.location())) return;
        RegistryBlocker.LOGGER.debug("[RegistryLoader] Blocking registry entry: {}", key);
        throw new RuntimeException("Blocked by RegistryBlocker");
    }
}