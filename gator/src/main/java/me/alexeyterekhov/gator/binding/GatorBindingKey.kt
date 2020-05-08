package me.alexeyterekhov.gator.binding

data class GatorBindingKey<T>(
    val type: Class<T>,
    val name: Any? = null
) {

    fun matches(type: Class<*>, name: Any? = null): Boolean =
        this.type == type && this.name == name
}