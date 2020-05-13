package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.binding.GatorBinding

class GatorBindingSet {

    private val typeToBinding = mutableMapOf<Class<*>, GatorBinding<*>>()
    private val typeToNameToBinding = mutableMapOf<Class<*>, MutableMap<Any, GatorBinding<*>>>()

    fun put(binding: GatorBinding<*>, override: Boolean) {
        binding.keys.forEach { bindingKey ->
            if (bindingKey.name == null) {
                putUnnamed(bindingKey.type, binding, override)
            } else {
                putNamed(bindingKey.type, bindingKey.name, binding, override)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(type: Class<T>, name: Any?): GatorBinding<out T>? {
        val binding = if (name == null) {
            typeToBinding[type]
        } else {
            typeToNameToBinding[type]?.get(name)
        }
        return binding?.let { it as GatorBinding<out T> }
    }

    private fun putUnnamed(type: Class<*>, binding: GatorBinding<*>, override: Boolean) {
        if (override || type !in typeToBinding) {
            typeToBinding[type] = binding
        }
    }

    private fun putNamed(type: Class<*>, name: Any, binding: GatorBinding<*>, override: Boolean) {
        var nameToBinding = typeToNameToBinding[type]
        if (nameToBinding == null) {
            nameToBinding = mutableMapOf()
            typeToNameToBinding[type] = nameToBinding
        }
        if (override || name !in nameToBinding) {
            nameToBinding[name] = binding
        }
    }
}