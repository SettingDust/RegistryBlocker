package settingdust.registry_blocker.util

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
