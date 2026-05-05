package settingdust.registry_blocker.v20_1.mixin;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import settingdust.registry_blocker.util.CommonIdentifier;

@Mixin(ResourceLocation.class)
public abstract class ResourceLocationMixin implements CommonIdentifier {
}
