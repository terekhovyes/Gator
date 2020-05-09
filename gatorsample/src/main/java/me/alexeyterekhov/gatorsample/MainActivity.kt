package me.alexeyterekhov.gatorsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import me.alexeyterekhov.gator.Gator
import me.alexeyterekhov.gator.scope.includeModule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appScope = Gator.openScope(application) {
            includeModule {
                single { Name("Alexey") }
                factory { Greeting(get()) }
            }
        }

        val activityScope = Gator.openScope(appScope, this) {
            includeModule {
                single { GreetingPrinter(get()) }
            }
        }

        val printer1 = activityScope.get<GreetingPrinter>()
        val printer2 = activityScope.get<GreetingPrinter>()
        printer1.print()
        printer2.print()
        Log.i("GatorSample", "Printer1: $printer1")
        Log.i("GatorSample", "Printer2: $printer2")

        val name1 = activityScope.get<Name>()
        val name2 = activityScope.get<Name>()
        Log.i("GatorSample", "name1: $name1")
        Log.i("GatorSample", "name2: $name2")

        val greeting1 = activityScope.get<Greeting>()
        val greeting2 = activityScope.get<Greeting>()
        Log.i("GatorSample", "greeting1: $greeting1")
        Log.i("GatorSample", "greeting2: $greeting2")

        val greetingProvider = appScope.provider<Greeting>()
        val greeting3 = greetingProvider()
        val greeting4 = greetingProvider()
        Log.i("GatorSample", "greeting3: $greeting3")
        Log.i("GatorSample", "greeting4: $greeting4")

        val greeting5 by appScope.lazy<Greeting>()
        Log.i("GatorSample", "greeting5: $greeting5")
        Log.i("GatorSample", "greeting5: $greeting5")
    }
}
