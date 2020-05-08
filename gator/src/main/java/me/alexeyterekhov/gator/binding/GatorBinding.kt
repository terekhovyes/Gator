package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.scope.GatorScope

class GatorBinding<T>(
    val keys: List<GatorBindingKey<in T>>,
    val provider: GatorProvider<out T>
) {

    fun matches(type: Class<*>, name: Any? = null): Boolean =
        keys.any { matches(type, name) }

    fun get(scope: GatorScope): T =
        provider.get(scope)
}