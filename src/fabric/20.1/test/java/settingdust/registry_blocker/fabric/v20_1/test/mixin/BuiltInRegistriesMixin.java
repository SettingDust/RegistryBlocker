package settingdust.registry_blocker.fabric.v20_1.test.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.registry_blocker.util.RegistryBlockerConfig;
import settingdust.registry_blocker.util.ValidatedDynamicMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registryblocker$setupTestBlockedEntries(CallbackInfo ci) {
        Map<ResourceLocation, List<ResourceLocation>> testEntries = new LinkedHashMap<>();
        testEntries.put(
            new ResourceLocation("minecraft", "item"),
            List.of(new ResourceLocation("minecraft", "apple"))
        );
        testEntries.put(
            new ResourceLocation("minecraft", "block"),
            List.of(new ResourceLocation("minecraft", "barrier"))
        );
        //noinspection unchecked, rawtypes
        ((ValidatedDynamicMap) RegistryBlockerConfig.Companion.getBlockedEntries()).setAndUpdate(testEntries);
    }
}
