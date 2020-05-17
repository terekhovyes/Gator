package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.binding.GatorBinding
import me.alexeyterekhov.gator.module.GatorModule

class GatorScope(
    val key: Any? = null,
    val parent: GatorScope? = null
) {

    private val bindingSet = GatorBindingSet()

    fun include(vararg modules: GatorModule, override: Boolean = false) =
        include(modules.toList(), override)

    fun include(modules: Collection<GatorModule>, override: Boolean = false) {
        modules
            .flatMap { it.bindings }
            .forEach { bindingSet.put(it, override) }
    }

    inline fun <reified T> instance(name: Any? = null): T =
        instance(T::class.java, name)

    inline fun <reified T> provider(name: Any? = null): () -> T =
        provider(T::class.java, name)

    inline fun <reified T> lazy(name: Any? = null): Lazy<T> =
        lazy(T::class.java, name)

    fun <T> instance(type: Class<T>, name: Any? = null): T =
        binding(type, name).provider.instance(this)

    fun <T> provider(type: Class<T>, name: Any? = null): () -> T =
        binding(type, name).let { binding ->
            { binding.provider.instance(this) }
        }

    fun <T> lazy(type: Class<T>, name: Any? = null): Lazy<T> =
        binding(type, name).let { binding ->
            kotlin.lazy { binding.provider.instance(this) }
        }

    private fun <T> binding(type: Class<T>, name: Any?): GatorBinding<out T> {
        val binding = bindingSet.get(type, name)
        return when {
            binding != null -> binding
            parent != null -> parent.binding(type, name)
            else -> throw IllegalStateException("Binding not found for ${string(type, name)}")
        }
    }

    private fun string(type: Class<*>, name: Any?): String {
        return if (name != null) {
            "type=$type name=$name"
        } else {
            "type=$type"
        }
    }
}