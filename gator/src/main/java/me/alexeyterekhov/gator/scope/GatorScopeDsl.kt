package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.module.GatorModuleInitializer
import me.alexeyterekhov.gator.module.module

typealias GatorScopeInitializer = GatorScope.() -> Unit

fun scope(
    key: Any? = null,
    parent: GatorScope? = null,
    init: GatorScopeInitializer? = null
) = GatorScope(key, parent).apply { init?.invoke(this) }

fun GatorScope.subScope(
    key: Any? = null,
    init: GatorScopeInitializer? = null
) = GatorScope(key, this).apply { init?.invoke(this) }

fun GatorScope.include(init: GatorModuleInitializer): Unit =
    include(module(init))