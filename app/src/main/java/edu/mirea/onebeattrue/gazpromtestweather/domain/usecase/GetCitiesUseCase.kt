package edu.mirea.onebeattrue.gazpromtestweather.domain.usecase

import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.repository.CityRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(): List<City> {
        return repository.getCities()
    }
}