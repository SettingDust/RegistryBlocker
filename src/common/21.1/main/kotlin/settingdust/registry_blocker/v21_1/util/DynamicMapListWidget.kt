@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package settingdust.registry_blocker.v21_1.util

import me.fzzyhmstrs.fzzy_config.entry.EntryValidator
import me.fzzyhmstrs.fzzy_config.entry.Entry as Entry1
import me.fzzyhmstrs.fzzy_config.screen.SuggestionWindowListener
import me.fzzyhmstrs.fzzy_config.screen.SuggestionWindowProvider
import me.fzzyhmstrs.fzzy_config.screen.widget.LayoutWidget
import me.fzzyhmstrs.fzzy_config.screen.widget.PopupWidget
import me.fzzyhmstrs.fzzy_config.screen.widget.TextureIds
import me.fzzyhmstrs.fzzy_config.screen.widget.custom.CustomButtonWidget
import me.fzzyhmstrs.fzzy_config.util.ValidationResult
import me.fzzyhmstrs.fzzy_config.validation.misc.ChoiceValidator
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors
import settingdust.registry_blocker.util.DynamicMapPopupFactory
import settingdust.registry_blocker.util.MinecraftVersion
import java.util.function.BiFunction
import java.util.function.Function

//client
class DynamicMapPopupFactory : DynamicMapPopupFactory {
    init {
        MinecraftVersion.V1211.requireCurrent()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K, V> openPopup(
        storedValue: Map<K, V>,
        keyHandler: Entry1<K, *>,
        valueHandlerFactory: (K) -> Entry1<V, *>,
        title: Component,
        onClose: (Map<K, V>) -> Unit,
        xPosition: BiFunction<Int, Int, Int>,
        yPosition: BiFunction<Int, Int, Int>,
    ) {
        try {
            val map = storedValue.map {
                Pair(
                    (keyHandler.instanceEntry() as Entry1<K, *>).also { entry -> entry.accept(it.key) },
                    (valueHandlerFactory(it.key).instanceEntry() as Entry1<V, *>).also { entry -> entry.accept(it.value) }
                )
            }.associate { it }

            val choiceValidator: BiFunction<DynamicMapListWidget<K, V>, DynamicMapListWidget.DynamicMapEntry<K, V>?, ChoiceValidator<K>> =
                BiFunction { ll, le -> DynamicMapListWidget.ExcludeSelfChoiceValidator(le) { self -> ll.getRawMap(self) } }

            val mapWidget = DynamicMapListWidget(map, keyHandler, valueHandlerFactory, choiceValidator)

            val popup = PopupWidget.Builder(title)
                .add("map", mapWidget, LayoutWidget.Position.BELOW, LayoutWidget.Position.ALIGN_LEFT)
                .addDoneWidget()
                .onClose { onClose(mapWidget.getMap()) }
                .positionX(xPosition)
                .positionY(yPosition)
                .build()

            PopupWidget.push(popup)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

internal class DynamicMapListWidget<K, V>(
    entryMap: Map<Entry1<K, *>, Entry1<V, *>>,
    keySupplier: Entry1<K, *>,
    valueHandlerFactory: (K) -> Entry1<V, *>,
    entryValidator: BiFunction<DynamicMapListWidget<K, V>, DynamicMapEntry<K, V>?, ChoiceValidator<K>>
) : ContainerObjectSelectionList<DynamicMapListWidget.DynamicMapEntry<K, V>>(
    Minecraft.getInstance(), 268, 160, 0, 22
), SuggestionWindowListener {

    private var suggestionWindowElement: GuiEventListener? = null

    override fun setSuggestionWindowElement(element: GuiEventListener?) {
        this.suggestionWindowElement = element
    }

    fun getMap(): Map<K, V> {
        val map: MutableMap<K, V> = mutableMapOf()
        for (e in this.children()) {
            if (e !is DynamicExistingEntry<K, V>) continue
            if (!e.isValid) continue
            val pair = e.get()
            map[pair.first] = pair.second
        }
        return map.toMap()
    }

    fun getRawMap(skip: DynamicMapEntry<K, V>? = null): Map<K, V> {
        val map: MutableMap<K, V> = mutableMapOf()
        for (e in this.children()) {
            if (e !is DynamicExistingEntry<K, V>) continue
            if (e == skip) continue
            val pair = e.get()
            map[pair.first] = pair.second
        }
        return map.toMap()
    }

    override fun renderListSeparators(guiGraphics: GuiGraphics) {
    }

    override fun renderListBackground(guiGraphics: GuiGraphics) {
    }

    override fun getRowWidth(): Int {
        return 258
    }

    override fun getRowLeft(): Int {
        return this.x
    }

    override fun getScrollbarPosition(): Int {
        return this.x + this.width - 6
    }

    private fun makeVisible(entry: DynamicMapEntry<K, V>) {
        this.ensureVisible(entry)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (suggestionWindowElement?.mouseClicked(mouseX, mouseY, button) == true) return true
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (suggestionWindowElement?.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
            ?: getChildAt(mouseX, mouseY).filter { element: GuiEventListener -> element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) }.isPresent
        ) return true
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (suggestionWindowElement?.keyPressed(keyCode, scanCode, modifiers) == true) return true
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    init {
        for (e in entryMap) {
            this.addEntry(DynamicExistingEntry(e.key, e.value, this, valueHandlerFactory, entryValidator))
        }
        this.addEntry(DynamicNewEntry(keySupplier, valueHandlerFactory, this, entryValidator))
    }

    private class DynamicExistingEntry<K, V>(
        private val key: Entry1<K, *>,
        private var value: Entry1<V, *>,
        private val parent: DynamicMapListWidget<K, V>,
        private val valueHandlerFactory: (K) -> Entry1<V, *>,
        keyValidator: BiFunction<DynamicMapListWidget<K, V>, DynamicMapEntry<K, V>?, ChoiceValidator<K>>
    ) : DynamicMapEntry<K, V>() {

        private var clickedWidget: GuiEventListener? = null
        private var lastKey: K = key.get()

        private var keyWidget = key.widgetAndTooltipEntry(keyValidator.apply(parent, this))
            .also { if (it is SuggestionWindowProvider) it.addListener(parent) }
        private var valueWidget = value.widgetAndTooltipEntry(ChoiceValidator.any())
            .also { if (it is SuggestionWindowProvider) it.addListener(parent) }

        private val deleteWidget = CustomButtonWidget.builder { parent.removeEntry(this) }
            .textures(TextureIds.DELETE, TextureIds.DELETE_INACTIVE, TextureIds.DELETE_HIGHLIGHTED)
            .tooltip(TextureIds.DELETE_LANG)
            .narrationSupplier { _, _ -> TextureIds.DELETE_LANG }
            .size(20, 20)
            .build()

        fun get(): Pair<K, V> {
            return Pair(key.get(), value.get())
        }

        private fun rebuildValueIfKeyChanged() {
            val currentKey = key.get()
            if (currentKey != lastKey) {
                lastKey = currentKey
                @Suppress("UNCHECKED_CAST")
                value = valueHandlerFactory(currentKey).instanceEntry() as Entry1<V, *>
                valueWidget = value.widgetAndTooltipEntry(ChoiceValidator.any())
                    .also { if (it is SuggestionWindowProvider) it.addListener(parent) }
            }
        }

        override fun children(): MutableList<out GuiEventListener> {
            return mutableListOf(keyWidget, valueWidget, deleteWidget)
        }

        override fun narratables(): MutableList<out NarratableEntry> {
            return mutableListOf(keyWidget, valueWidget, deleteWidget)
        }

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
            getChildAt(mouseX, mouseY).ifPresentOrElse({ clickedWidget = it }, { clickedWidget = null })
            return super.mouseClicked(mouseX, mouseY, button)
        }

        override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (clickedWidget != null) {
                return (clickedWidget?.mouseReleased(mouseX, mouseY, button) ?: super.mouseReleased(mouseX, mouseY, button)).also { clickedWidget = null }
            }
            return super.mouseReleased(mouseX, mouseY, button)
        }

        override fun render(
            guiGraphics: GuiGraphics,
            index: Int,
            top: Int,
            left: Int,
            width: Int,
            height: Int,
            mouseX: Int,
            mouseY: Int,
            hovering: Boolean,
            partialTick: Float
        ) {
            rebuildValueIfKeyChanged()
            keyWidget.setPosition(left, top)
            keyWidget.render(guiGraphics, mouseX, mouseY, partialTick)
            guiGraphics.drawString(parent.minecraft.font, TextureIds.MAP_ARROW, left + 115, top + 5, CommonColors.WHITE)
            valueWidget.setPosition(left + 124, top)
            valueWidget.render(guiGraphics, mouseX, mouseY, partialTick)
            deleteWidget.setPosition(left + 238, top)
            deleteWidget.render(guiGraphics, mouseX, mouseY, partialTick)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private class DynamicNewEntry<K, V>(
        private val keySupplier: Entry1<K, *>,
        private val valueHandlerFactory: (K) -> Entry1<V, *>,
        private val parent: DynamicMapListWidget<K, V>,
        private val validator: BiFunction<DynamicMapListWidget<K, V>, DynamicMapEntry<K, V>?, ChoiceValidator<K>>
    ) : DynamicMapEntry<K, V>() {

        private val addWidget = CustomButtonWidget.builder {
            parent.removeEntry(this)
            val newKey = keySupplier.instanceEntry() as Entry1<K, *>
            val newValue = valueHandlerFactory(newKey.get()).instanceEntry() as Entry1<V, *>
            parent.addEntry(DynamicExistingEntry(newKey, newValue, parent, valueHandlerFactory, validator))
            parent.addEntry(this)
            parent.makeVisible(this)
        }
            .textures(TextureIds.ADD, TextureIds.ADD_INACTIVE, TextureIds.ADD_HIGHLIGHTED)
            .tooltip(TextureIds.ADD_LANG)
            .narrationSupplier { _, _ -> TextureIds.ADD_LANG }
            .size(20, 20)
            .build()

        override fun children(): MutableList<out GuiEventListener> {
            return mutableListOf(addWidget)
        }

        override fun narratables(): MutableList<out NarratableEntry> {
            return mutableListOf(addWidget)
        }

        override fun render(
            guiGraphics: GuiGraphics,
            index: Int,
            top: Int,
            left: Int,
            width: Int,
            height: Int,
            mouseX: Int,
            mouseY: Int,
            hovering: Boolean,
            partialTick: Float
        ) {
            addWidget.setPosition(left + 238, top)
            addWidget.render(guiGraphics, mouseX, mouseY, partialTick)
        }
    }

    abstract class DynamicMapEntry<K, V> : Entry<DynamicMapEntry<K, V>>() {
        var isValid = true
    }

    internal class ExcludeSelfChoiceValidator<K, V>(
        private val self: DynamicMapEntry<K, V>?,
        private val disallowed: Function<DynamicMapEntry<K, V>?, Map<K, V>>
    ) : ChoiceValidator<K>(ValuesPredicate(null, null)) {
        override fun validateEntry(input: K, type: EntryValidator.ValidationType): ValidationResult<K> {
            if (self == null) return ValidationResult.success(input)
            return ValidationResult.predicated(
                input,
                !disallowed.apply(self).containsKey(input),
                ValidationResult.Errors.INVALID
            ) { b ->
                b.content("No duplicate map keys")
            }.also { self.isValid = it.isValid() }
        }
    }
}
