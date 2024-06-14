package edu.mirea.onebeattrue.gazpromtestweather.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.DefaultCityComponent
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.DefaultWeatherComponent
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val cityComponentFactory: DefaultCityComponent.Factory,
    private val weatherComponentFactory: DefaultWeatherComponent.Factory,

    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Cities,
        handleBackButton = true,
        childFactory = ::child,
        key = "root"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        Config.Cities -> {
            val component = cityComponentFactory.create(
                onCityItemClicked = { city ->
                    navigation.pushNew(Config.Weather(city))
                },
                componentContext = componentContext
            )
            RootComponent.Child.Cities(component)
        }

        is Config.Weather -> {
            val component = weatherComponentFactory.create(
                city = config.city,
                onBackClicked = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            RootComponent.Child.Weather(component)
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data class Weather(val city: City) : Config

        @Serializable
        data object Cities : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}