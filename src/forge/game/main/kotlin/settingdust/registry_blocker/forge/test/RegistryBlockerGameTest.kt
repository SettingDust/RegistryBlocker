package settingdust.registry_blocker.forge.test

import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraftforge.gametest.GameTestHolder
import settingdust.registry_blocker.test.RegistryBlockingGameTests

@GameTestHolder("registry_blocker")
class RegistryBlockerGameTest : RegistryBlockingGameTests {
    @GameTest(template = "empty")
    override fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        super.testBlockMultipleRegistryTypes(helper)
    }
}