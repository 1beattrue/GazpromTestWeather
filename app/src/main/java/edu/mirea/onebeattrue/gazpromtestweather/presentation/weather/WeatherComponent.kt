package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface WeatherComponent {

    val city: City

    val model: StateFlow<WeatherStore.State>

    fun onClickBack()

    fun onUpdateClick()
}