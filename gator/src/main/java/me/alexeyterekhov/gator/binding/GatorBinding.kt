package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.provider.GatorProvider

class GatorBinding<T>(
    val targets: List<GatorBindingTarget<in T>>,
    val provider: GatorProvider<out T>
)