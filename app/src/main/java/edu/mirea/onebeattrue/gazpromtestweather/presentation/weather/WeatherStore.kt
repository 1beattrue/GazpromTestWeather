package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import edu.mirea.onebeattrue.gazpromtestweather.domain.usecase.GetForecastUseCase
import edu.mirea.onebeattrue.gazpromtestweather.domain.usecase.GetWeatherUseCase
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.WeatherStore.Intent
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.WeatherStore.Label
import edu.mirea.onebeattrue.gazpromtestweather.presentation.weather.WeatherStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WeatherStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data object Update : Intent
    }

    data class State(
        val weatherState: WeatherState, val forecastState: ForecastState
    ) {
        sealed interface WeatherState {
            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(val weather: Weather) : WeatherState
        }

        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data object Error : ForecastState
            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

class WeatherStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) {

    fun create(
        city: City
    ): WeatherStore =
        object : WeatherStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                weatherState = State.WeatherState.Initial,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = { ExecutorImpl(city) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object WeatherLoading : Action
        data object WeatherLoadingError : Action
        data class WeatherLoaded(val weather: Weather) : Action
        data object ForecastLoading : Action
        data object ForecastLoadingError : Action
        data class ForecastLoaded(val forecast: Forecast) : Action
    }

    private sealed interface Msg {
        data object WeatherLoading : Msg
        data object WeatherLoadingError : Msg
        data class WeatherLoaded(val weather: Weather) : Msg
        data object ForecastLoading : Msg
        data object ForecastLoadingError : Msg
        data class ForecastLoaded(val forecast: Forecast) : Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.WeatherLoading)
                try {
                    val weather = withContext(Dispatchers.IO) {
                        getWeatherUseCase(
                            lat = city.lat,
                            lon = city.lon
                        )
                    }
                    dispatch(Action.WeatherLoaded(weather))
                } catch (e: Exception) {
                    e.printStackTrace()
                    dispatch(Action.WeatherLoadingError)
                }
            }

            scope.launch {
                dispatch(Action.ForecastLoading)
                try {
                    val forecast = withContext(Dispatchers.IO) {
                        getForecastUseCase(
                            lat = city.lat,
                            lon = city.lon
                        )
                    }
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    e.printStackTrace()
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val city: City
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var weatherLoadingJob: Job? = null
        private var forecastLoadingJob: Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.Update -> {
                    weatherLoadingJob?.cancel()
                    weatherLoadingJob = scope.launch {
                        dispatch(Msg.WeatherLoading)
                        try {
                            val weather = withContext(Dispatchers.IO) {
                                getWeatherUseCase(
                                    lat = city.lat,
                                    lon = city.lon
                                )
                            }
                            dispatch(Msg.WeatherLoaded(weather))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            dispatch(Msg.WeatherLoadingError)
                        }
                    }

                    forecastLoadingJob?.cancel()
                    forecastLoadingJob = scope.launch {
                        dispatch(Msg.ForecastLoading)
                        try {
                            val forecast = withContext(Dispatchers.IO) {
                                getForecastUseCase(
                                    lat = city.lat,
                                    lon = city.lon
                                )
                            }
                            dispatch(Msg.ForecastLoaded(forecast))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            dispatch(Msg.ForecastLoadingError)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoading -> {
                    dispatch(Msg.ForecastLoading)
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }

                is Action.WeatherLoaded -> {
                    dispatch(Msg.WeatherLoaded(action.weather))
                }

                Action.WeatherLoading -> {
                    dispatch(Msg.ForecastLoading)
                }

                Action.WeatherLoadingError -> {
                    dispatch(Msg.WeatherLoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ForecastLoaded -> copy(
                forecastState = State.ForecastState.Loaded(msg.forecast)
            )

            Msg.ForecastLoading -> copy(
                forecastState = State.ForecastState.Loading
            )

            Msg.ForecastLoadingError -> copy(
                forecastState = State.ForecastState.Error
            )

            is Msg.WeatherLoaded -> copy(
                weatherState = State.WeatherState.Loaded(msg.weather)
            )

            Msg.WeatherLoading -> copy(
                weatherState = State.WeatherState.Loading
            )

            Msg.WeatherLoadingError -> copy(
                weatherState = State.WeatherState.Error
            )
        }
    }

    companion object {
        private const val STORE_NAME = "WeatherStore"
    }
}
