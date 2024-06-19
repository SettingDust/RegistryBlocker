package settingdust.registryblocker.mixin;

import net.minecraft.server.DataPackContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.registryblocker.EntrypointKt;
import settingdust.registryblocker.RegistryBlocker;

import java.util.concurrent.CompletableFuture;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    private static void registryblocker$reload(
        final CallbackInfoReturnable<CompletableFuture<DataPackContents>> cir
    ) {
        RegistryBlocker.INSTANCE.reload();
    }
}
