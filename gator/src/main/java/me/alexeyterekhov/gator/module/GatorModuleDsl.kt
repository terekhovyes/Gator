package me.alexeyterekhov.gator.module

import me.alexeyterekhov.gator.binding.GatorBinding
import me.alexeyterekhov.gator.binding.GatorBindingKey
import me.alexeyterekhov.gator.provider.GatorFactoryProvider
import me.alexeyterekhov.gator.provider.GatorProvider
import me.alexeyterekhov.gator.provider.GatorProviderFunction
import me.alexeyterekhov.gator.provider.GatorSingleProvider

typealias GatorModuleInitializer = GatorModuleDsl.() -> Unit

fun module(init: GatorModuleInitializer): GatorModule {
    val moduleDsl = GatorModuleDsl()
    moduleDsl.init()
    return moduleDsl.moduleBuilder.build()
}

class GatorModuleDsl {

    val moduleBuilder = GatorModuleBuilder()

    inline fun <reified T> single(noinline providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        single(T::class.java, null, providerFunction)

    inline fun <reified T> factory(noinline providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        factory(T::class.java, null, providerFunction)

    inline fun <reified T> single(name: Any?, noinline providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        single(T::class.java, name, providerFunction)

    inline fun <reified T> factory(name: Any?, noinline providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        factory(T::class.java, name, providerFunction)

    fun <T> single(type: Class<T>, providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        single(type, null, providerFunction)

    fun <T> factory(type: Class<T>, providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> =
        factory(type, null, providerFunction)

    fun <T> single(type: Class<T>, name: Any?, providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> {
        val singleProvider = GatorSingleProvider(providerFunction)
        val bindingKey = GatorBindingKey(type, name)
        return GatorBindingBuilder<T>()
            .apply { provider = singleProvider }
            .apply { keys += bindingKey }
            .also { moduleBuilder.bindingBuilders += it }
    }

    fun <T> factory(type: Class<T>, name: Any?, providerFunction: GatorProviderFunction<out T>): GatorBindingBuilder<T> {
        val factoryProvider = GatorFactoryProvider(providerFunction)
        val bindingKey = GatorBindingKey(type, name)
        return GatorBindingBuilder<T>()
            .apply { provider = factoryProvider }
            .apply { keys += bindingKey }
            .also { moduleBuilder.bindingBuilders += it }
    }

    infix fun <T> GatorBindingBuilder<T>.alsoBinds(type: Class<in T>): GatorBindingBuilder<T> =
        alsoBinds(GatorBindingKey(type))

    fun <T> GatorBindingBuilder<T>.alsoBinds(type: Class<in T>, name: Any?): GatorBindingBuilder<T> =
        alsoBinds(GatorBindingKey(type, name))

    infix fun <T> GatorBindingBuilder<T>.alsoBinds(bindingKey: GatorBindingKey<in T>): GatorBindingBuilder<T> =
        apply { keys += bindingKey }
}

class GatorModuleBuilder {

    val bindings = mutableListOf<GatorBinding<*>>()
    val bindingBuilders = mutableListOf<GatorBindingBuilder<*>>()

    fun build(): GatorModule =
        GatorModule(bindings + bindingBuilders.map { it.build() })
}

class GatorBindingBuilder<T> {

    val keys = mutableListOf<GatorBindingKey<in T>>()
    var provider: GatorProvider<out T>? = null

    fun build(): GatorBinding<T> =
        GatorBinding(keys, checkNotNull(provider))
}