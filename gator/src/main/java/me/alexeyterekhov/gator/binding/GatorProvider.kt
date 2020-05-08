package me.alexeyterekhov.gator.binding

import me.alexeyterekhov.gator.scope.GatorScope

interface GatorProvider<T> {
    fun get(scope: GatorScope): T
}