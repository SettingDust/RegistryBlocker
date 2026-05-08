package settingdust.registry_blocker.v26_1.test.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
        if (!registryblocker$isGameTest()) {
            return;
        }

        Map<Identifier, List<Identifier>> testEntries = new LinkedHashMap<>();
        testEntries.put(
            Identifier.fromNamespaceAndPath("minecraft", "item"),
            List.of(Identifier.fromNamespaceAndPath("minecraft", "apple"))
        );
        testEntries.put(
            Identifier.fromNamespaceAndPath("minecraft", "block"),
            List.of(Identifier.fromNamespaceAndPath("minecraft", "barrier"))
        );
        //noinspection unchecked, rawtypes
        ((ValidatedDynamicMap) RegistryBlockerConfig.Companion.getBlockedEntries()).setAndUpdate(testEntries);
    }

    @Unique
    private static boolean registryblocker$isGameTest() {
        if (registryblocker$hasClass("net.fabricmc.loader.api.FabricLoader")) {
            return System.getProperty("fabric-api.gametest") != null;
        }
        if (registryblocker$hasClass("net.minecraftforge.fml.loading.FMLLoader")) {
            return System.getProperty("forge.enabledGameTestNamespaces") != null;
        }
        if (registryblocker$hasClass("net.neoforged.fml.loading.FMLLoader")) {
            return System.getProperty("neoforge.enabledGameTestNamespaces") != null;
        }
        return false;
    }

    @Unique
    private static boolean registryblocker$hasClass(String name) {
        try {
            Class.forName(name, false, BuiltInRegistriesMixin.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}