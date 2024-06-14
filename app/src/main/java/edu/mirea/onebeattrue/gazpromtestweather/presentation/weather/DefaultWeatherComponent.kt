package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultWeatherComponent @AssistedInject constructor(
    private val storeFactory: WeatherStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : WeatherComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    WeatherStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<WeatherStore.State>
        get() = store.stateFlow

    override fun onClickBack() {
        store.accept(WeatherStore.Intent.ClickBack)
    }

    override fun onUpdateClick() {
        store.accept(WeatherStore.Intent.Update)
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("city") city: City,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultWeatherComponent
    }
}