package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.scope.GatorScope

class GatorProviderSingle<T>(private val factory: GatorScope.() -> T) : GatorProvider<T> {

    private var singleInstance: T? = null
    private var isInitialized = false

    @Suppress("UNCHECKED_CAST")
    override fun get(scope: GatorScope): T {
        if (isInitialized) {
            return singleInstance as T
        }
        synchronized(this) {
            if (!isInitialized) {
                isInitialized = true
                singleInstance = scope.factory()
            }
        }
        return singleInstance as T
    }
}