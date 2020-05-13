package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.provider.GatorProvider

class GatorBinding<T>(
    val keys: List<GatorBindingKey<in T>>,
    val provider: GatorProvider<out T>
)