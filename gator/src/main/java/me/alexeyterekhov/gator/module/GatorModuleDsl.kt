package me.alexeyterekhov.gator.module

import me.alexeyterekhov.gator.binding.*
import me.alexeyterekhov.gator.scope.GatorScope

fun module(init: GatorModuleContext.() -> Unit): GatorModule {
    return GatorModuleBuilder()
        .apply { init() }
        .build()
}

abstract class GatorModuleContext {

    inline fun <reified T> single(noinline factory: GatorScope.() -> T): GatorBindingContext<T> =
        single(T::class.java, factory)

    inline fun <reified T> single(name: Any, noinline factory: GatorScope.() -> T): GatorBindingContext<T> =
        single(T::class.java, name, factory)

    inline fun <reified T> factory(noinline factory: GatorScope.() -> T): GatorBindingContext<T> =
        factory(T::class.java, factory)

    inline fun <reified T> factory(name: Any, noinline factory: GatorScope.() -> T): GatorBindingContext<T> =
        factory(T::class.java, name, factory)

    abstract fun <T> single(type: Class<T>, factory: GatorScope.() -> T): GatorBindingContext<T>
    abstract fun <T> single(type: Class<T>, name: Any, factory: GatorScope.() -> T): GatorBindingContext<T>
    abstract fun <T> factory(type: Class<T>, factory: GatorScope.() -> T): GatorBindingContext<T>
    abstract fun <T> factory(type: Class<T>, name: Any, factory: GatorScope.() -> T): GatorBindingContext<T>
}

interface GatorBindingContext<T> {
    infix fun alsoBinds(type: Class<in T>): GatorBindingContext<T>
    fun alsoBinds(type: Class<in T>, name: Any): GatorBindingContext<T>
}

private class GatorBindingBuilder<T>(
    private val provider: GatorProvider<out T>
) : GatorBindingContext<T> {

    val keys = mutableListOf<GatorBindingKey<in T>>()

    fun build(): GatorBinding<T> =
        GatorBinding(keys, provider)

    override fun alsoBinds(type: Class<in T>): GatorBindingContext<T> {
        this.keys += GatorBindingKey(type)
        return this
    }

    override fun alsoBinds(type: Class<in T>, name: Any): GatorBindingContext<T> {
        this.keys += GatorBindingKey(type, name)
        return this
    }
}

private class GatorModuleBuilder : GatorModuleContext() {

    private val bindingBuilders = mutableListOf<GatorBindingBuilder<*>>()

    fun build(): GatorModule =
        GatorModule(
            bindingBuilders.map { it.build() }
        )

    override fun <T> single(type: Class<T>, factory: GatorScope.() -> T): GatorBindingContext<T> {
        val provider = GatorProviderSingle(factory)
        val key = GatorBindingKey(type)
        return GatorBindingBuilder(provider)
            .apply { keys += key }
            .also { bindingBuilders += it }
    }

    override fun <T> single(type: Class<T>, name: Any, factory: GatorScope.() -> T): GatorBindingContext<T> {
        val provider = GatorProviderSingle(factory)
        val key = GatorBindingKey(type, name)
        return GatorBindingBuilder(provider)
            .apply { keys += key }
            .also { bindingBuilders += it }
    }

    override fun <T> factory(type: Class<T>, factory: GatorScope.() -> T): GatorBindingContext<T> {
        val provider = GatorProviderFactory(factory)
        val key = GatorBindingKey(type)
        return GatorBindingBuilder(provider)
            .apply { keys += key }
            .also { bindingBuilders += it }
    }

    override fun <T> factory(type: Class<T>, name: Any, factory: GatorScope.() -> T): GatorBindingContext<T> {
        val provider = GatorProviderFactory(factory)
        val key = GatorBindingKey(type, name)
        return GatorBindingBuilder(provider)
            .apply { keys += key }
            .also { bindingBuilders += it }
    }
}