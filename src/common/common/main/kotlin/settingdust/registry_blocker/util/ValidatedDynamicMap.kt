@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package settingdust.registry_blocker.util

import me.fzzyhmstrs.fzzy_config.entry.Entry
import me.fzzyhmstrs.fzzy_config.entry.EntryOpener
import me.fzzyhmstrs.fzzy_config.entry.EntryValidator
import me.fzzyhmstrs.fzzy_config.screen.widget.LayoutWidget
import me.fzzyhmstrs.fzzy_config.screen.widget.PopupWidget
import me.fzzyhmstrs.fzzy_config.screen.widget.TextureIds
import me.fzzyhmstrs.fzzy_config.screen.widget.custom.CustomButtonWidget
import me.fzzyhmstrs.fzzy_config.util.ValidationResult
import me.fzzyhmstrs.fzzy_config.util.ValidationResult.Companion.attachTo
import me.fzzyhmstrs.fzzy_config.validation.ValidatedLazyField
import me.fzzyhmstrs.fzzy_config.validation.collection.MapListWidget
import me.fzzyhmstrs.fzzy_config.validation.misc.ChoiceValidator
import net.minecraft.client.gui.components.AbstractWidget
import net.peanuuutz.tomlkt.TomlElement
import net.peanuuutz.tomlkt.TomlLiteral
import net.peanuuutz.tomlkt.TomlTableBuilder
import net.peanuuutz.tomlkt.asTomlTable
import java.util.function.BiFunction

/**
 * A Validated Map where value handlers are dynamically determined by the key.
 *
 * Unlike [me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedIdentifierMap], which uses a fixed
 * valueHandler for all entries, this class uses a [valueHandlerFactory] that produces a value handler
 * based on the current key. This enables per-key value validation and serialization logic.
 *
 * @param K the key type
 * @param V the value type
 * @param defaultValue the default map
 * @param keyHandler [Entry] for handling keys
 * @param valueHandlerFactory factory that produces the appropriate value [Entry] for a given key
 */
open class ValidatedDynamicMap<K, V>(
    defaultValue: Map<K, V>,
    private val keyHandler: Entry<K, *>,
    private val valueHandlerFactory: (K) -> Entry<V, *>
) : ValidatedLazyField<Map<K, V>>(defaultValue, { mapOf() }),
    Map<K, V>, EntryOpener {

    init {
        for ((key, value) in defaultValue) {
            if (keyHandler.validateEntry(key, EntryValidator.ValidationType.WEAK).isError())
                throw IllegalStateException("Default Map key [$key] not valid per keyHandler provided")
            val valueHandler = valueHandlerFactory(key)
            if (valueHandler.validateEntry(value, EntryValidator.ValidationType.WEAK).isError())
                throw IllegalStateException("Default Map value [$value] not valid per valueHandler for key [$key]")
        }
        compositeFlags(keyHandler)
    }

    override val handler: BiFunction<TomlElement, String, ValidationResult<Map<K, V>>>? =
        BiFunction { te, fn -> deserialize(te, fn) }

    override fun deserialize(toml: TomlElement, fieldName: String): ValidationResult<Map<K, V>> {
        return try {
            val table = toml.asTomlTable()
            val map: MutableMap<K, V> = mutableMapOf()
            val keyErrors = ValidationResult.createMutable("Skipped keys")
            val valueErrors = ValidationResult.createMutable("Value errors")
            for (entry in table.entries) {
                val (key, el) = entry
                val keyResult = keyHandler.deserializeEntry(TomlLiteral(key), "{$fieldName, @key: $key}", 65)
                    .attachTo(keyErrors)
                if (!keyResult.isValid()) {
                    continue
                }
                val parsedKey = keyResult.get()
                val valueHandler = valueHandlerFactory(parsedKey)
                val valueResult = valueHandler.deserializeEntry(el, "{$fieldName, @key: $key}", 65).attachTo(valueErrors)
                map[parsedKey] = valueResult.get()
            }
            val totalErrors = ValidationResult.createMutable("Errors found deserializing dynamic map [$fieldName]")
            totalErrors.addError(keyErrors)
            totalErrors.addError(valueErrors)
            ValidationResult.ofMutable(map, totalErrors)
        } catch (e: Throwable) {
            ValidationResult.error(defaultValue, ValidationResult.Errors.DESERIALIZATION, "Exception during dynamic map [$fieldName] deserialization, using default map", e)
        }
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun serialize(input: Map<K, V>): ValidationResult<TomlElement> {
        val table = TomlTableBuilder()
        return try {
            val valueErrors = ValidationResult.createMutable("Value errors")
            for ((key, value) in input) {
                val valueHandler = valueHandlerFactory(key)
                val el = valueHandler.serializeEntry(value, 1).attachTo(valueErrors)
                table.element(key.toString(), el.get(), emptyList())
            }
            ValidationResult.ofMutable(table.build(), valueErrors)
        } catch (e: Throwable) {
            ValidationResult.error(table.build(), ValidationResult.Errors.SERIALIZATION, "Exception encountered serializing dynamic map", e)
        }
    }

    @Suppress("SafeCastWithReturn", "UNCHECKED_CAST")
    override fun deserializedChanged(old: Any?, new: Any?): Boolean {
        old as? Map<K, V> ?: return true
        new as? Map<K, V> ?: return true
        val checked: MutableList<K> = mutableListOf()
        for ((k, v) in old) {
            if (!new.containsKey(k)) return true
            val valueHandler = valueHandlerFactory(k)
            if (valueHandler.deserializedChanged(v, new[k])) return true
            checked.add(k)
        }
        for ((k, _) in new) {
            if (checked.contains(k)) continue
            return true
        }
        return false
    }

    override fun correctEntry(input: Map<K, V>, type: EntryValidator.ValidationType): ValidationResult<Map<K, V>> {
        val map: MutableMap<K, V> = mutableMapOf()
        val keyErrors = ValidationResult.createMutable("Key errors")
        val valueErrors = ValidationResult.createMutable("Value errors")
        for ((key, value) in input) {
            val keyResult = keyHandler.validateEntry(key, type).attachTo(keyErrors)
            if (keyResult.isError()) {
                continue
            }
            val valueHandler = valueHandlerFactory(key)
            map[key] = valueHandler.correctEntry(value, type).attachTo(valueErrors).get()
        }
        val totalErrors = ValidationResult.createMutable("Map correction found errors")
        totalErrors.addError(keyErrors)
        totalErrors.addError(valueErrors)
        return ValidationResult.ofMutable(map, totalErrors)
    }

    override fun validateEntry(input: Map<K, V>, type: EntryValidator.ValidationType): ValidationResult<Map<K, V>> {
        val keyErrors = ValidationResult.createMutable("Key errors")
        val valueErrors = ValidationResult.createMutable("Value errors")
        for ((key, value) in input) {
            keyHandler.validateEntry(key, type).attachTo(keyErrors)
            val valueHandler = valueHandlerFactory(key)
            valueHandler.validateEntry(value, type).attachTo(valueErrors)
        }
        val totalErrors = ValidationResult.createMutable("Map validation found errors")
        totalErrors.addError(keyErrors)
        totalErrors.addError(valueErrors)
        return ValidationResult.ofMutable(input, totalErrors)
    }

    override fun instanceEntry(): ValidatedDynamicMap<K, V> {
        return this.copyProvidersTo(ValidatedDynamicMap(storedValue, keyHandler, valueHandlerFactory))
    }

    @Suppress("UNCHECKED_CAST")
    override fun isValidEntry(input: Any?): Boolean {
        if (input !is Map<*, *>) return false
        return try {
            validateEntry(input as Map<K, V>, EntryValidator.ValidationType.STRONG).isValid()
        } catch (e: Throwable) {
            false
        }
    }

    override fun copyValue(input: Map<K, V>): Map<K, V> {
        return input.toMap()
    }

    //client
    override fun open(args: List<String>) {
        openMapEditPopup()
    }

    //client
    @Suppress("UNCHECKED_CAST", "INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    private fun openMapEditPopup(
        xPosition: BiFunction<Int, Int, Int> = PopupWidget.Builder.center(),
        yPosition: BiFunction<Int, Int, Int> = PopupWidget.Builder.center()
    ) {
        try {
            val map = storedValue.map {
                Pair(
                    (keyHandler.instanceEntry() as Entry<K, *>).also { entry -> entry.accept(it.key) },
                    (valueHandlerFactory(it.key).instanceEntry() as Entry<V, *>).also { entry -> entry.accept(it.value) }
                )
            }.associate { it }

            val defaultValueHandler = valueHandlerFactory(keyHandler.get())

            val choiceValidator: BiFunction<MapListWidget<K, V>, MapListWidget.MapEntry<K, V>?, ChoiceValidator<K>> =
                BiFunction { ll, le -> MapListWidget.ExcludeSelfChoiceValidator(le) { self -> ll.getRawMap(self) } }

            val mapWidget = MapListWidget(map, keyHandler, defaultValueHandler, choiceValidator)

            val popup = PopupWidget.Builder(this.translation())
                .add("map", mapWidget, LayoutWidget.Position.BELOW, LayoutWidget.Position.ALIGN_LEFT)
                .addDoneWidget()
                .onClose { this.setAndUpdate(mapWidget.getMap()) }
                .positionX(xPosition)
                .positionY(yPosition)
                .build()

            PopupWidget.push(popup)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    //client
    override fun widgetEntry(choicePredicate: ChoiceValidator<Map<K, V>>): AbstractWidget {
        return CustomButtonWidget.builder(TextureIds.MAP_LANG) { b: CustomButtonWidget ->
            openMapEditPopup(
                PopupWidget.Builder.popupContext { w -> b.x + b.width / 2 - w / 2 },
                PopupWidget.Builder.popupContext { h -> b.y + b.height / 2 - h / 2 }
            )
        }.size(110, 20).build()
    }

    // Map interface delegation
    override val entries get() = get().entries
    override val keys get() = get().keys
    override val size get() = get().size
    override val values get() = get().values
    override fun containsKey(key: K) = get().containsKey(key)
    override fun containsValue(value: V) = get().containsValue(value)
    override fun get(key: K) = get()[key]
    override fun isEmpty() = get().isEmpty()
}
