package me.alexeyterekhov.gator

import me.alexeyterekhov.gator.scope.GatorScope

object Gator {

    private val keyScope = mutableMapOf<Any, GatorScope>()

    fun openScope(vararg keys: Any, init: (GatorScope.() -> Unit)? = null): GatorScope {
        check(keys.isNotEmpty()) { "Keys mustn't be empty to open scope" }
        val key = keys.last()
        val existingScope = keyScope[key]
        return existingScope ?: newScope(keys, init)
    }

    fun openScope(parent: GatorScope, key: Any, init: (GatorScope.() -> Unit)? = null): GatorScope {
        check(parent.key in keyScope) { "Parent scope must be present in Gator to open sub scope" }
        val existingScope = keyScope[key]
        return existingScope ?: newScope(parent, key, init)
    }

    fun scope(key: Any): GatorScope? =
        keyScope[key]

    fun scopeExists(key: Any): Boolean =
        key in keyScope

    fun dropScope(key: Any) {
        val scope = keyScope[key]
        if (scope != null) {
            val childrenScopes = keyScope.values.filter { it.parent === scope }
            childrenScopes.forEach { dropScope(it.key) }
            keyScope.remove(key)
        }
    }

    private fun newScope(keys: Array<out Any>, init: (GatorScope.() -> Unit)?): GatorScope {
        val parentScope = parentScope(keys)
        val key = keys.last()
        return newScope(parentScope, key, init)
    }

    private fun newScope(parentScope: GatorScope?, key: Any, init: (GatorScope.() -> Unit)?): GatorScope {
        val newScope = GatorScope(key, parentScope).also {
            keyScope[key] = it
        }
        if (init != null) {
            newScope.init()
        }
        return newScope
    }

    private fun parentScope(keys: Array<out Any>): GatorScope? {
        val parentScopeKeys = keys.dropLast(1).toTypedArray()
        return if (parentScopeKeys.isNotEmpty()) {
            openScope(*parentScopeKeys)
        } else {
            null
        }
    }
}