package settingdust.registry_blocker.fabric.v26_1.test

import java.lang.reflect.Method
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import settingdust.registry_blocker.test.RegistryBlockingGameTests

class RegistryBlockerGameTest : CustomTestMethodInvoker, RegistryBlockingGameTests {
    override fun invokeTestMethod(helper: GameTestHelper, method: Method) {
        method.invoke(this, helper)
    }

    @GameTest
    override fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        super.testBlockMultipleRegistryTypes(helper)
    }
}
