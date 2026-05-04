package settingdust.registry_blocker.util

class Event<T>(private val invokerFactory: (List<T>) -> T) {
    private val listeners = mutableListOf<T>()
    private var invoker: T = invokerFactory(listeners)

    private fun updateInvoker() {
        invoker = invokerFactory(listeners)
    }

    fun register(listener: T) {
        listeners.add(listener)
        updateInvoker()
    }

    fun invoker(): T = invoker
}
