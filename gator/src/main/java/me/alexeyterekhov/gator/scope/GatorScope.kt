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

    fun <T> get(type: Class<T>, name: Any? = null): T {
        return find(
            type,
            name,
            fromBinding = { binding -> binding.get(this) },
            fromParent = { parent -> parent.get(type, name) }
        )
    }

    fun <T> provider(type: Class<T>, name: Any? = null): () -> T {
        return find(
            type,
            name,
            fromBinding = { binding ->
                { binding.get(this) }
            },
            fromParent = { parent -> parent.provider(type, name) }
        )
    }

    fun <T> lazy(type: Class<T>, name: Any? = null): Lazy<T> {
        return find(
            type,
            name,
            fromBinding = { binding ->
                kotlin.lazy { binding.get(this) }
            },
            fromParent = { parent -> parent.lazy(type, name) }
        )
    }

    private inline fun <T, R> find(
        type: Class<T>,
        name: Any?,
        fromBinding: (GatorBinding<T>) -> R,
        fromParent: (GatorScope) -> R
    ): R {
        val binding = bindingSet.binding(type, name)
        return when {
            binding != null -> fromBinding(binding)
            parent != null -> fromParent(parent)
            else -> throw IllegalStateException("Binding not found for type=$type${if (name != null) " name=$name" else ""}")
        }
    }
}