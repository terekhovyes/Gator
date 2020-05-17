# Gator
Gator is tiny DI lib. Koin syntax. Toothpick scope model

# Example
```Kotlin
class Person(val name: String)

class GreetingPrinter(val person: Person) {
    fun print() = println("Hello, ${person.name}!")
}

Gator.scope(application) {
    include {
        factory { GreetingPrinter(instance()) }
    }
}

val screenScope = Gator.scope(application, screen) {
    include {
        single { Person("Gator") }
    }
}

val greetingPrinter = screenScope.instance<GreetingPrinter>()
greetingPrinter.print() // "Hello, Gator!"
```
