package me.alexeyterekhov.gator.provider

import me.alexeyterekhov.gator.scope.GatorScope

typealias GatorProviderFunction<T> = GatorScope.() -> T