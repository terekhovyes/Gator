package me.alexeyterekhov.gator.binding

class GatorBinding<T>(
    val keys: List<GatorBindingKey<in T>>,
    val provider: GatorProvider<out T>
)