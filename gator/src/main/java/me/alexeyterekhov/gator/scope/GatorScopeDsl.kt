package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.module.GatorModuleInitializer
import me.alexeyterekhov.gator.module.module

typealias GatorScopeInitializer = GatorScope.() -> Unit

fun scope(
    parent: GatorScope? = null,
    init: GatorScopeInitializer? = null
): GatorScope =
    GatorScope(parent).apply { init?.invoke(this) }

fun GatorScope.subScope(
    init: GatorScopeInitializer? = null
): GatorScope =
    GatorScope(this).apply { init?.invoke(this) }

fun GatorScope.include(init: GatorModuleInitializer): Unit =
    include(module(init))