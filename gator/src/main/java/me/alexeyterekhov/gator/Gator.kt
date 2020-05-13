package me.alexeyterekhov.gator

import me.alexeyterekhov.gator.scope.GatorScope
import me.alexeyterekhov.gator.scope.GatorScopeInitializer
import me.alexeyterekhov.gator.scope.scope

object Gator {

    private val keyToScope = mutableMapOf<Any, GatorScope>()

    fun scope(vararg keys: Any, init: GatorScopeInitializer? = null): GatorScope {
        check(keys.isNotEmpty()) { "Gator requires key to open scope" }
        val key = keys.last()
        val alreadyOpenedScope = keyToScope[key]
        return alreadyOpenedScope ?: newScope(keys, init)
    }

    fun scope(parent: GatorScope, key: Any, init: GatorScopeInitializer? = null): GatorScope {
        check(parent.key in keyToScope) { "Gator requires parent scope to be registered in Gator" }
        val alreadyOpenedScope = keyToScope[key]
        return alreadyOpenedScope ?: newScope(parent, key, init)
    }

    fun scopeOrNull(key: Any): GatorScope? =
        keyToScope[key]

    fun scopeExists(key: Any): Boolean =
        key in keyToScope

    fun dropScope(key: Any) {
        val scopeToDrop = keyToScope[key]
        if (scopeToDrop != null) {
            val childrenScopes = keyToScope.values.filter { it.parent === scopeToDrop }
            childrenScopes.forEach { childScope ->
                if (childScope.key != null) {
                    dropScope(childScope.key)
                }
            }
            keyToScope.remove(key)
        }
    }

    private fun newScope(keys: Array<out Any>, init: GatorScopeInitializer?): GatorScope {
        val parent = parentScope(keys)
        val key = keys.last()
        return newScope(parent, key, init)
    }

    private fun newScope(parent: GatorScope?, key: Any, init: GatorScopeInitializer?): GatorScope =
        scope(key, parent, init).also {
            keyToScope[key] = it
        }

    private fun parentScope(keys: Array<out Any>): GatorScope? {
        val parentKeys = keys.dropLast(1).toTypedArray()
        return if (parentKeys.isNotEmpty()) scope(*parentKeys) else null
    }
}