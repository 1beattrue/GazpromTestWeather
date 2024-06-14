package edu.mirea.onebeattrue.gazpromtestweather.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.CityContent
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.WeatherContent

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade() + slide())
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Cities -> CityContent(component = instance.component)
            is RootComponent.Child.Weather -> WeatherContent(component = instance.component)
        }
    }
}