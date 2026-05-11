package settingdust.registry_blocker.neoforge.v21_1.test

import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.neoforged.neoforge.gametest.GameTestHolder
import settingdust.registry_blocker.test.RegistryBlockingGameTests

@GameTestHolder("registry_blocker")
class RegistryBlockerGameTest : RegistryBlockingGameTests {
    @GameTest(template = "empty")
    override fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        super.testBlockMultipleRegistryTypes(helper)
    }
}