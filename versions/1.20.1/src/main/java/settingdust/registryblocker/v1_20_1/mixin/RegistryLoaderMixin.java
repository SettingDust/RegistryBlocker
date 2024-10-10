package settingdust.registryblocker.v1_20_1.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.serialization.Decoder;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.registryblocker.RegistryBlocker;

import java.util.Map;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @Inject(
        method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/Resource;getReader()Ljava/io/BufferedReader;")
    )
    private static <E> void registryblocker$filterBlocked(
        final RegistryOps.RegistryInfoGetter registryInfoGetter,
        final ResourceManager resourceManager,
        final RegistryKey<? extends Registry<E>> key,
        final MutableRegistry<E> newRegistry,
        final Decoder<E> decoder,
        final Map<RegistryKey<?>, Exception> exceptions,
        final CallbackInfo ci,
        @Local ResourceFinder resourceFinder,
        @Share("blocked") LocalBooleanRef blocked
    ) {
        if (!RegistryBlocker.INSTANCE.getConfig().containsKey(key.getRegistry())) return;
        var blacklist = RegistryBlocker.INSTANCE.getConfig().get(key.getRegistry());
        if (!blacklist.contains(key.getValue())) return;
        RegistryBlocker.LOGGER.debug("[RegistryLoader] Blocking registry entry: {}", key);
        blocked.set(true);
        throw new RuntimeException("Blocked by RegistryBlocker");
    }

    @WrapWithCondition(
        method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private static <K, V> boolean registryblocker$dontRecordBlocked(
        final Map instance,
        final K k,
        final V v,
        @Share("blocked") LocalBooleanRef blocked
    ) {
        return !blocked.get();
    }
}
