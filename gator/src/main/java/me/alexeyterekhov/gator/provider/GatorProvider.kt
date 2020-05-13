package me.alexeyterekhov.gator.provider

import me.alexeyterekhov.gator.scope.GatorScope

interface GatorProvider<T> {
    fun value(scope: GatorScope): T
}