package settingdust.registry_blocker.test

import net.minecraft.gametest.framework.GameTestHelper
import settingdust.registry_blocker.util.CommonIdentifier
import settingdust.registry_blocker.util.RegistryLookupAdapter

interface RegistryBlockingGameTests {
    fun assertRegistryEntryBlocked(
        helper: GameTestHelper,
        registryId: CommonIdentifier,
        blockedEntryId: CommonIdentifier,
        allowedEntryId: CommonIdentifier
    ) {
        val registryLookupAdapter = RegistryLookupAdapter
        val blockedExists = registryLookupAdapter.containsEntry(registryId, blockedEntryId)
        val allowedExists = registryLookupAdapter.containsEntry(registryId, allowedEntryId)

        helper.assertTrue(!blockedExists, "Expected blocked entry to be absent: $registryId -> $blockedEntryId")
        helper.assertTrue(allowedExists, "Expected non-blocked entry to exist: $registryId -> $allowedEntryId")
    }

    fun testBlockMultipleRegistryTypes(helper: GameTestHelper) {
        assertRegistryEntryBlocked(
            helper,
            CommonIdentifier.parse("minecraft:item"),
            CommonIdentifier.parse("minecraft:apple"),
            CommonIdentifier.parse("minecraft:stone")
        )
        assertRegistryEntryBlocked(
            helper,
            CommonIdentifier.parse("minecraft:block"),
            CommonIdentifier.parse("minecraft:barrier"),
            CommonIdentifier.parse("minecraft:stone")
        )

        helper.succeed()
    }
}
