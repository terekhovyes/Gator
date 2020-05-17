package me.alexeyterekhov.gator

import me.alexeyterekhov.gator.scope.GatorScope
import me.alexeyterekhov.gator.scope.GatorScopeInitializer
import me.alexeyterekhov.gator.scope.scope

object Gator {

    private val keyToScopeEntry = mutableMapOf<Any, GatorScopeEntry>()

    fun scope(vararg keys: Any, init: GatorScopeInitializer? = null): GatorScope {
        check(keys.isNotEmpty()) { "Gator requires key to open scope" }
        val key = keys.last()
        return scopeOrNull(key) ?: newScope(keys, init)
    }

    fun scope(parent: GatorScope, key: Any, init: GatorScopeInitializer? = null): GatorScope {
        val parentEntry = entry(parent)
        checkNotNull(parentEntry) { "Gator requires parent scope to be registered in Gator" }
        return scopeOrNull(key) ?: newScope(parentEntry, key, init)
    }

    fun scopeOrNull(key: Any): GatorScope? =
        keyToScopeEntry[key]?.scope

    fun scopeExists(key: Any): Boolean =
        key in keyToScopeEntry

    fun dropScope(key: Any) {
        synchronized(keyToScopeEntry) {
            keyToScopeEntry.values.removeAll { scopeEntry ->
                key == scopeEntry.key || key in scopeEntry.parentKeys
            }
        }
    }

    private fun entry(scope: GatorScope): GatorScopeEntry? =
        keyToScopeEntry.values.firstOrNull { it.scope === scope }

    private fun newScope(keys: Array<out Any>, init: GatorScopeInitializer?): GatorScope =
        newScope(
            parent = parentScope(keys),
            parentKeys = keys.dropLast(1),
            key = keys.last(),
            init = init
        )

    private fun newScope(parentEntry: GatorScopeEntry?, key: Any, init: GatorScopeInitializer?): GatorScope =
        newScope(
            parent = parentEntry?.scope,
            parentKeys = parentEntry?.keys ?: emptyList(),
            key = key,
            init = init
        )

    private fun newScope(parent: GatorScope?, parentKeys: List<Any>, key: Any, init: GatorScopeInitializer?): GatorScope =
        scope(parent, init).also {
            synchronized(keyToScopeEntry) {
                keyToScopeEntry[key] = GatorScopeEntry(parentKeys, key, it)
            }
        }

    private fun parentScope(keys: Array<out Any>): GatorScope? =
        if (keys.size > 1) {
            val parentKeys = keys.dropLast(1).toTypedArray()
            scope(*parentKeys)
        } else {
            null
        }

    private class GatorScopeEntry(
        val parentKeys: List<Any>,
        val key: Any,
        val scope: GatorScope
    ) {
        val keys: List<Any>
            get() = parentKeys + key
    }
}