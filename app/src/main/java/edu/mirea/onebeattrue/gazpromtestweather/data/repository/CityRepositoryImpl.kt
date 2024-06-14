package edu.mirea.onebeattrue.gazpromtestweather.data.repository

import edu.mirea.onebeattrue.gazpromtestweather.data.mapper.toEntity
import edu.mirea.onebeattrue.gazpromtestweather.data.network.api.CityApiService
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityApiService: CityApiService
) : CityRepository {
    override suspend fun getCities(): List<City> {
        return cityApiService.getCities().map { it.toEntity() }
    }
}