package settingdust.registry_blocker.v26_1.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import settingdust.registry_blocker.BlockingRegistry;
import settingdust.registry_blocker.RegistryBlocker;

import javax.annotation.Nullable;

@Mixin(Holder.Reference.class)
public class HolderReferenceMixin<T> {
    @Shadow
    @Nullable
    private ResourceKey<T> key;

    @Shadow
    @Nullable
    private T value;

    @Final
    @Shadow
    private HolderOwner<T> owner;

    @SuppressWarnings("unchecked")
    @WrapMethod(method = "key")
    private ResourceKey<T> registryblocker$getKeyWithFallback(final Operation<ResourceKey<T>> original) {
        if (this.key != null) {
            return original.call();
        }
        if (owner instanceof BlockingRegistry<?> blocking) {
            for (final var entry : ((BlockingRegistry<T>) blocking).getRegistryblocker$blocked().entrySet()) {
                if (entry.getValue() == this.value) {
                    RegistryBlocker.LOGGER.debug(
                        "[Holder.Reference#key] Fallback to blocked entry for: {}",
                        entry.getKey()
                    );
                    return entry.getKey();
                }
            }
        }
        return original.call();
    }
}
