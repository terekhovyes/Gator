package me.alexeyterekhov.gator.scope.internal

import me.alexeyterekhov.gator.binding.GatorBinding
import me.alexeyterekhov.gator.module.GatorModule

class GatorBindingSet {

    private val typeBinding = mutableMapOf<Class<*>, GatorBinding<*>>()
    private val typeNameBinding = mutableMapOf<Class<*>, MutableMap<Any, GatorBinding<*>>>()

    fun include(modules: Array<out GatorModule>) =
        modules.forEach(::include)

    fun include(module: GatorModule) =
        module.bindings.forEach(::include)

    fun include(binding: GatorBinding<*>) {
        binding.keys.forEach { key ->
            if (key.name == null) {
                include(key.type, binding)
            } else {
                include(key.type, key.name, binding)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> binding(type: Class<T>, name: Any?): GatorBinding<T>? {
        val untypedBinding = if (name == null) {
            typeBinding[type]
        } else {
            typeNameBinding[type]?.get(name)
        }
        return untypedBinding?.let { it as GatorBinding<T> }
    }

    private fun include(type: Class<*>, binding: GatorBinding<*>) {
        if (type !in typeBinding) {
            typeBinding[type] = binding
        }
    }

    private fun include(type: Class<*>, name: Any, binding: GatorBinding<*>) {
        val nameBinding = nameBinding(type)
        if (name !in nameBinding) {
            nameBinding[name] = binding
        }
    }

    private fun nameBinding(type: Class<*>): MutableMap<Any, GatorBinding<*>> {
        val existingMap = typeNameBinding[type]
        return if (existingMap != null) {
            existingMap
        } else {
            val newMap = mutableMapOf<Any, GatorBinding<*>>()
            typeNameBinding[type] = newMap
            newMap
        }
    }
}