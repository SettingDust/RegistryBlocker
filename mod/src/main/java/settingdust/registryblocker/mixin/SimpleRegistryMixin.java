package settingdust.registryblocker.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registryblocker.EntrypointKt;

@Mixin(SimpleRegistry.class)
public class SimpleRegistryMixin {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void registryblocker$filterRegistries(
            final RegistryKey<?> key,
            final Object entry,
            final Lifecycle lifecycle,
            final CallbackInfoReturnable<RegistryEntry.Reference<?>> cir) {
        if (EntrypointKt.getConfig().containsKey(key.getRegistry())
                && EntrypointKt.getConfig().get(key.getRegistry()).contains(key.getValue())) cir.setReturnValue(null);
    }
}
