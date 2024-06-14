package edu.mirea.onebeattrue.gazpromtestweather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.GazpromTestWeatherTheme

class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WeatherApplication).component.inject(this)
        super.onCreate(savedInstanceState)

//        val component = rootComponentFactory.create(defaultComponentContext())

        enableEdgeToEdge()
        setContent {
            GazpromTestWeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    RootContent(
//                        modifier = Modifier.padding(innerPadding),
//                        component = component
//                    )
                    Text(
                        modifier = Modifier.padding(innerPadding),
                        text = "Hello world!"
                    )
                }
            }
        }
    }
}