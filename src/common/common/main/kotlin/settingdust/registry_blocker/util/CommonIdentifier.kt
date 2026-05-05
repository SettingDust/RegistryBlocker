package settingdust.registry_blocker.util

/**
 * Common interface for resource identifiers across MC versions.
 * Can be extended with Mixin injection into MC's native ResourceLocation/Identifier classes.
 */
interface CommonIdentifier {
    val namespace: String
    val path: String

    companion object {
        fun of(namespace: String, path: String): CommonIdentifier =
            IdentifierAdapter.create(namespace, path)

        fun parse(value: String): CommonIdentifier =
            IdentifierAdapter.parse(value)
    }
}
