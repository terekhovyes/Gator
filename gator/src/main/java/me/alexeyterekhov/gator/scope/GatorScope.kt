package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.module.GatorModule

class GatorScope(
    val key: Any,
    val parent: GatorScope? = null
) {
    fun include(vararg modules: GatorModule): Unit = TODO()
    inline fun <reified T : Any> get(): T = TODO()
    inline fun <reified T : Any> getProvider(): () -> T = TODO()
    inline fun <reified T : Any> lazy(): Lazy<T> = TODO()
    fun <T : Any> get(type: Class<T>): T = TODO()
    fun <T : Any> getProvider(type: Class<T>): () -> T = TODO()
    fun <T : Any> lazy(type: Class<T>): Lazy<T> = TODO()
}