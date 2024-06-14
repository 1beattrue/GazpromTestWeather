package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface CityComponent {

    val model: StateFlow<CityStore.State>

    fun onUpdateClick()

    fun onCityItemClick(city: City)
}