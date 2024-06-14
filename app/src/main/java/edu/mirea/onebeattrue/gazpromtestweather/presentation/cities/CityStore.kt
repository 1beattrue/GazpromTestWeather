package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.usecase.GetCitiesUseCase
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.CityStore.Intent
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.CityStore.Label
import edu.mirea.onebeattrue.gazpromtestweather.presentation.cities.CityStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CityStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class CityItemClick(val city: City) : Intent
        data object Update : Intent
    }

    data class State(
        val screenState: ScreenState
    ) {
        sealed interface ScreenState {
            data object Initial : ScreenState
            data object Loading : ScreenState
            data object Error : ScreenState
            data class Loaded(
                val cities: List<Category>
            ) : ScreenState
        }
    }

    sealed interface Label {
        data class CityItemClick(val city: City) : Label
    }
}

class CityStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCitiesUseCase: GetCitiesUseCase
) {

    fun create(): CityStore =
        object : CityStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(State.ScreenState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class CitiesLoaded(val cities: List<City>) : Action
        data object Loading : Action
        data object LoadingError : Action
    }

    private sealed interface Msg {
        data class CitiesLoaded(val cities: List<Category>) : Msg
        data object Loading : Msg
        data object LoadingError : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
                try {
                    val cities = withContext(Dispatchers.IO) { getCitiesUseCase() }
                    dispatch(Action.CitiesLoaded(cities))
                } catch (e: Exception) {
                    e.printStackTrace()
                    dispatch(Action.LoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var loadingJob: Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.CityItemClick -> {
                    publish(Label.CityItemClick(intent.city))
                }

                Intent.Update -> {
                    loadingJob?.cancel()
                    loadingJob = scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            val cities = withContext(Dispatchers.IO) { getCitiesUseCase() }
                                .toMapWithHeaders()
                            dispatch(Msg.CitiesLoaded(cities))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            dispatch(Msg.LoadingError)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.CitiesLoaded -> {
                    val cities = action.cities.toMapWithHeaders()
                    dispatch(Msg.CitiesLoaded(cities))
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }

                Action.LoadingError -> {
                    dispatch(Msg.LoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.CitiesLoaded -> copy(screenState = State.ScreenState.Loaded(msg.cities))
                Msg.Loading -> copy(screenState = State.ScreenState.Loading)
                Msg.LoadingError -> copy(screenState = State.ScreenState.Error)
            }
    }

    private fun List<City>.toMapWithHeaders(): List<Category> {
        return groupBy { it.name.first() }.toSortedMap().map { category ->
            Category(
                name = category.key.toString(),
                items = category.value.sortedBy { it.name }
            )
        }
    }

    companion object {
        private const val STORE_NAME = "CityStore"
    }
}

data class Category(
    val name: String,
    val items: List<City>
)