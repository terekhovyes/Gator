package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.scope.GatorScope

class GatorProviderFactory<T>(private val factory: GatorScope.() -> T) : GatorProvider<T> {

    override fun get(scope: GatorScope): T = scope.factory()
}