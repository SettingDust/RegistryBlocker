package settingdust.registry_blocker.v1_21.mixin;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.registry_blocker.RegistryBlocker;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @ModifyExpressionValue(
        method = "loadContentsFromManager",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
        )
    )
    private static <E> Iterator<Map.Entry<ResourceLocation, Resource>> registryblocker$filterBlocked(
        Iterator<Map.Entry<ResourceLocation, Resource>> original,
        @Local(argsOnly = true) WritableRegistry<E> registry
    ) {
        var key = registry.key();
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