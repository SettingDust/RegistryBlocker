package settingdust.registry_blocker.v26_1.mixin;

import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import settingdust.registry_blocker.util.CommonIdentifier;

@Mixin(Identifier.class)
public abstract class IdentifierMixin implements CommonIdentifier {
}
