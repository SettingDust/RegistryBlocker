package settingdust.registry_blocker.v1_20.mixin;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.registry_blocker.RegistryBlocker;

import java.util.Iterator;
import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @ModifyExpressionValue(
        method = "loadRegistryContents",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
        )
    )
    private static <E> Iterator<Map.Entry<ResourceLocation, Resource>> registryblocker$filterBlocked(
        Iterator<Map.Entry<ResourceLocation, Resource>> original,
        @Local(argsOnly = true) ResourceKey<? extends Registry<E>> key
    ) {
        var blacklist = RegistryBlocker.INSTANCE.getConfig().get(key.location());
        if (blacklist == null) return original;
        return Iterators.filter(
            original, it -> {
                var blocked = blacklist.contains(it.getKey());
                if (blocked) {
                    RegistryBlocker.LOGGER.debug(
                        "[RegistryDataLoader] Blocking registry entry: {} {}",
                        key.location(),
                        it
                    );
                }
                return !blocked;
            }
        );
    }
}