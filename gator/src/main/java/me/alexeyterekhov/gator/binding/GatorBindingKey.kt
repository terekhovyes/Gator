package me.alexeyterekhov.gator.binding

class GatorBindingKey<T>(
    val type: Class<T>,
    val name: Any? = null
)