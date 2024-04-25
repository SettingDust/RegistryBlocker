package settingdust.registryblocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import settingdust.registryblocker.EntrypointKt;
import settingdust.registryblocker.RegistryBlocker;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {
    @ModifyExpressionValue(
        method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;" +
                 "Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;" +
                 "Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;entrySet()Ljava/util/Set;"
        )
    )
    private static Set<Map.Entry<Identifier, Resource>> registryblocker$filterBlocked(
        final Set<Map.Entry<Identifier, Resource>> original,
        @Local(argsOnly = true) RegistryKey<? extends Registry<?>> registryRef,
        @Local ResourceFinder resourceFinder
    ) {
        if (!EntrypointKt.getConfig().containsKey(registryRef.getValue())) return original;
        var blacklist = EntrypointKt.getConfig().get(registryRef.getValue());
        return original.stream().filter(it -> {
            var id = resourceFinder.toResourceId(it.getKey());
            var blocked = blacklist.contains(id);
            if (blocked) RegistryBlocker.LOGGER.debug(
                "[RegistryLoader] Blocking registry entry: {} / {}",
                registryRef.getValue(),
                id
            );
            return !blocked;
        }).collect(Collectors.toSet());
    }
}
