package edu.mirea.onebeattrue.gazpromtestweather.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.CityComponent
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.WeatherComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Weather(val component: WeatherComponent) : Child()
        class Cities(val component: CityComponent) : Child()
    }
}