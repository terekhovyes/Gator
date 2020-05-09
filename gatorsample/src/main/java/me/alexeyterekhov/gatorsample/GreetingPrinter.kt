package me.alexeyterekhov.gatorsample

import android.util.Log

class GreetingPrinter(val greeting: Greeting) {
    fun print() {
        Log.i("GatorSample", greeting.asString())
    }
}