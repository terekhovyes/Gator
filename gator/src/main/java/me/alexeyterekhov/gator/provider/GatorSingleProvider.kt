package me.alexeyterekhov.gator.provider

import me.alexeyterekhov.gator.scope.GatorScope

class GatorSingleProvider<T>(
    private val providerFunction: GatorProviderFunction<out T>
) : GatorProvider<T> {

    private var singleInstance: T? = null
    private var isCreated = false

    @Suppress("UNCHECKED_CAST")
    override fun instance(scope: GatorScope): T {
        if (!isCreated) {
            synchronized(this) {
                if (!isCreated) {
                    isCreated = true
                    singleInstance = scope.providerFunction()
                }
            }
        }
        return singleInstance as T
    }
}