package me.alexeyterekhov.gator.provider

import me.alexeyterekhov.gator.scope.GatorScope

class GatorFactoryProvider<T>(
    private val providerFunction: GatorProviderFunction<out T>
) : GatorProvider<T> {

    override fun instance(scope: GatorScope): T =
        scope.providerFunction()
}