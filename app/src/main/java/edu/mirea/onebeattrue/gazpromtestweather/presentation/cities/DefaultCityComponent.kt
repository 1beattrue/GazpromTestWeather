package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

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

class DefaultCityComponent @AssistedInject constructor(
    private val storeFactory: CityStoreFactory,
    @Assisted("onCityItemClicked") private val onCityItemClicked: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : CityComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is CityStore.Label.CityItemClick -> {
                        onCityItemClicked(it.city)
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CityStore.State>
        get() = store.stateFlow

    override fun onUpdateClick() {
        store.accept(CityStore.Intent.Update)
    }

    override fun onCityItemClick(city: City) {
        store.accept(CityStore.Intent.CityItemClick(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onCityItemClicked") onCityItemClicked: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultCityComponent
    }
}