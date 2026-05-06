@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package settingdust.registry_blocker.util

import me.fzzyhmstrs.fzzy_config.entry.Entry
import net.minecraft.network.chat.Component
import java.util.function.BiFunction

//client
interface DynamicMapPopupFactory {
    companion object : DynamicMapPopupFactory by ServiceLoaderUtil.findService()

    fun <K, V> openPopup(
        storedValue: Map<K, V>,
        keyHandler: Entry<K, *>,
        valueHandlerFactory: (K) -> Entry<V, *>,
        title: Component,
        onClose: (Map<K, V>) -> Unit,
        xPosition: BiFunction<Int, Int, Int>,
        yPosition: BiFunction<Int, Int, Int>,
    )
}
