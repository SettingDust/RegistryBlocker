package settingdust.registry_blocker

import settingdust.registry_blocker.util.Event

fun interface ServerReloadCallback {
    companion object {
        val EVENT = Event<ServerReloadCallback> { listeners ->
            ServerReloadCallback {
                listeners.forEach { it.onReload() }
            }
        }
    }

    fun onReload()
}
