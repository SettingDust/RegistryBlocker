package settingdust.registry_blocker.mixin

import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import settingdust.registry_blocker.util.LoaderAdapter
import settingdust.registry_blocker.util.MinecraftVersion

class MinecraftVersionMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) = Unit

    override fun getRefMapperConfig(): String? = null

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String): Boolean {
        return when {
            ".v20_1.test.mixin." in mixinClassName -> LoaderAdapter.isGameTest && MinecraftVersion.current == MinecraftVersion.V1201
            ".v21_1.test.mixin." in mixinClassName -> LoaderAdapter.isGameTest && MinecraftVersion.current == MinecraftVersion.V1211
            ".v26_1.test.mixin." in mixinClassName -> LoaderAdapter.isGameTest && MinecraftVersion.current == MinecraftVersion.V261
            ".v20_1." in mixinClassName -> MinecraftVersion.current == MinecraftVersion.V1201
            ".v21_1." in mixinClassName -> MinecraftVersion.current == MinecraftVersion.V1211
            ".v26_1." in mixinClassName -> MinecraftVersion.current == MinecraftVersion.V261
            ".test.mixin." in mixinClassName -> LoaderAdapter.isGameTest
            else -> true
        }
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) = Unit

    override fun getMixins(): MutableList<String>? = null

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) = Unit

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) = Unit
}
