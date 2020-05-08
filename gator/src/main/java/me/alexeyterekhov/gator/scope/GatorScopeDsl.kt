package me.alexeyterekhov.gator.scope

import me.alexeyterekhov.gator.module.GatorModuleContext
import me.alexeyterekhov.gator.module.module

fun GatorScope.includeModule(init: GatorModuleContext.() -> Unit) {
    val module = module(init)
    include(module)
}