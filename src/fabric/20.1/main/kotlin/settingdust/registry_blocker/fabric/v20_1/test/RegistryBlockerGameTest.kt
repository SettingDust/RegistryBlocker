package settingdust.registry_blocker.fabric.v20_1.test

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import settingdust.registry_blocker.test.RegistryBlockingGameTests

class RegistryBlockerGameTest : FabricGameTest, RegistryBlockingGameTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    override fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        super.testBlockMultipleRegistryTypes(helper)
    }
}

