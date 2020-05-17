# Gator [![](https://jitpack.io/v/terekhovyes/gator.svg)](https://jitpack.io/#terekhovyes/gator)
Gator is tiny DI lib. Koin syntax. Toothpick scope model

```
Root build.gradle file:
allprojects {
    repositories {
        ...
		maven { url 'https://jitpack.io' }
    }
}

App build.gradle file:
dependencies {
    implementation 'com.github.terekhovyes:gator:$past_last_version_here'
}
```

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
