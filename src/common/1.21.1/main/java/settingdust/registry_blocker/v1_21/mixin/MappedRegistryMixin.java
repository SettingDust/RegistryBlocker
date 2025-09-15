package settingdust.registry_blocker.v1_21.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import settingdust.registry_blocker.BlockingRegistry;
import settingdust.registry_blocker.RegistryBlocker;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements BlockingRegistry<T> {

    @Shadow
    @Final
    ResourceKey<? extends Registry<T>> key;

    @WrapMethod(method = "register")
    private Holder.Reference<T> registryblocker$filterRegistries(
        final ResourceKey<T> key,
        final T entry,
        final RegistrationInfo info,
        final Operation<Holder.Reference<T>> original
    ) {
        if (RegistryBlocker.INSTANCE.getConfig().containsKey(this.key.location())
            && RegistryBlocker.INSTANCE.getConfig().get(key.registry()).contains(key.location())) {
            getRegistryblocker$blocked().put(key, entry);
            RegistryBlocker.LOGGER.debug("[MappedRegistry#register] Blocking registry entry: {}", key);
            return null;
        }
        return original.call(key, entry, info);
    }
}