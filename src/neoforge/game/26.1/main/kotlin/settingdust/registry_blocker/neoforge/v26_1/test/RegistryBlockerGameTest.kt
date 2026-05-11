package settingdust.registry_blocker.neoforge.v26_1.test

import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.gametest.framework.GameTestInstance
import net.minecraft.gametest.framework.FunctionGameTestInstance
import net.minecraft.gametest.framework.TestData
import net.minecraft.gametest.framework.TestEnvironmentDefinition
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Rotation
import net.neoforged.neoforge.event.RegisterGameTestsEvent
import settingdust.registry_blocker.test.RegistryBlockingGameTests

object RegistryBlockerGameTest : RegistryBlockingGameTests {
    fun register(event: RegisterGameTestsEvent) {
        val environment = event.registerEnvironment(Identifier.fromNamespaceAndPath("registry_blocker", "default"))
        event.registerTest(
            Identifier.fromNamespaceAndPath("registry_blocker", "block_multiple_registry_types"),
            TestInstance(
                TestData(
                    environment,
                    Identifier.fromNamespaceAndPath("minecraft", "empty"),
                   100,
                   0,
                    true,
                    Rotation.NONE,
                ),
            ),
        )
    }

    override fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        super.testBlockMultipleRegistryTypes(helper)
    }

    private class TestInstance(
        testData: TestData<net.minecraft.core.Holder<TestEnvironmentDefinition<*>>>,
    ) : GameTestInstance(testData) {
        override fun run(helper: GameTestHelper) {
            testBlockMultipleRegistryTypes(helper)
        }

        override fun codec() = FunctionGameTestInstance.CODEC

        override fun typeDescription() = Component.literal("registry_blocker")
    }
}