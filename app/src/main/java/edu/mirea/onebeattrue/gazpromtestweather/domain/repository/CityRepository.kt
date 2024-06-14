package edu.mirea.onebeattrue.gazpromtestweather.domain.repository

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City

interface CityRepository {
    suspend fun getCities(): List<City>
}