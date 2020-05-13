package me.alexeyterekhov.gator.provider

import me.alexeyterekhov.gator.scope.GatorScope

class GatorSingleProvider<T>(
    private val providerFunction: GatorProviderFunction<out T>
) : GatorProvider<T> {

    private var single: T? = null
    private var isCreated = false

    @Suppress("UNCHECKED_CAST")
    override fun value(scope: GatorScope): T {
        if (!isCreated) {
            synchronized(this) {
                if (!isCreated) {
                    isCreated = true
                    single = scope.providerFunction()
                }
            }
        }
        return single as T
    }
}