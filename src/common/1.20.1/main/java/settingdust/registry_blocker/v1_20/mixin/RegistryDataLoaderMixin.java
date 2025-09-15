package settingdust.registry_blocker.v1_20.mixin;

import com.mojang.serialization.Decoder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.registry_blocker.RegistryBlocker;

import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Inject(
        method = "loadRegistryContents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/Resource;openAsReader()Ljava/io/BufferedReader;"
        )
    )
    private static <E> void registryblocker$filterBlocked(
        final RegistryOps.RegistryInfoLookup par1,
        final ResourceManager par2,
        final ResourceKey<? extends Registry<E>> key,
        final WritableRegistry<E> par4,
        final Decoder<E> par5,
        final Map<ResourceKey<?>, Exception> par6,
        final CallbackInfo ci
    ) {
        if (!RegistryBlocker.INSTANCE.getConfig().containsKey(key.registry())) return;
        var blacklist = RegistryBlocker.INSTANCE.getConfig().get(key.registry());
        if (!blacklist.contains(key.location())) return;
        RegistryBlocker.LOGGER.debug("[RegistryDataLoader] Blocking registry entry: {}", key);
        throw new RuntimeException("Blocked by RegistryBlocker");
    }
}