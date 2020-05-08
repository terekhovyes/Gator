package me.alexeyterekhov.gator

import me.alexeyterekhov.gator.scope.GatorScope

object Gator {
    fun openScope(vararg keys: Any, init: (GatorScope.() -> Unit)? = null): GatorScope = TODO()
    fun openScope(parent: GatorScope, key: Any, init: (GatorScope.() -> Unit)?): GatorScope = TODO()
    fun scope(key: Any): GatorScope? = TODO()
    fun scopeExists(key: Any): Boolean = TODO()
    fun dropScope(key: Any): Unit = TODO()
}