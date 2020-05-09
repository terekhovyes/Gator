package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.binding.GatorBinding
import me.alexeyterekhov.gator.module.GatorModule
import me.alexeyterekhov.gator.scope.internal.GatorBindingSet

class GatorScope(
    val key: Any,
    val parent: GatorScope? = null
) {

    private val bindingSet = GatorBindingSet()

    fun include(vararg modules: GatorModule) =
        bindingSet.include(modules)

    inline fun <reified T> get(name: Any? = null): T =
        get(T::class.java, name)

    inline fun <reified T> provider(name: Any? = null): () -> T =
        provider(T::class.java, name)

    inline fun <reified T> lazy(name: Any? = null): Lazy<T> =
        lazy(T::class.java, name)

    fun <T> get(type: Class<T>, name: Any? = null): T =
        binding(type, name).get(this)

    fun <T> provider(type: Class<T>, name: Any? = null): () -> T =
        binding(type, name).let { binding ->
            { binding.get(this) }
        }

    fun <T> lazy(type: Class<T>, name: Any? = null): Lazy<T> =
        binding(type, name).let { binding ->
            kotlin.lazy { binding.get(this) }
        }

    private fun <T> binding(type: Class<T>, name: Any?): GatorBinding<T> {
        val binding = bindingSet.binding(type, name)
        return when {
            binding != null -> binding
            parent != null -> parent.binding(type, name)
            else -> throw IllegalStateException("Binding not found for ${toString(type, name)}")
        }
    }

    private fun toString(type: Class<*>, name: Any?): String {
        val typeString = "type=$type"
        val nameString = name?.let { " name=$it" } ?: ""
        return typeString + nameString
    }
}