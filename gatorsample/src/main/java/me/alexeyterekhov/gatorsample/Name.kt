package me.alexeyterekhov.gatorsample

class Name(val string: String) {
    override fun toString(): String {
        return "$string@${hashCode()}"
    }
}