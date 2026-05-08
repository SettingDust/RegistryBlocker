package settingdust.registry_blocker.v26_1.mixin;

import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.resources.RegistryLoadTask$PendingRegistration")
public interface PendingRegistrationAccessor {
    @Accessor("key")
    ResourceKey<?> registryblocker$getKey();
}