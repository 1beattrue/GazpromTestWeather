package edu.mirea.onebeattrue.gazpromtestweather.data.mapper

import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.CityDto
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City


fun CityDto.toEntity(): City = City(
    id = id.toLong(),
    name = name,
    lat = lat.toFloat(),
    lon = lon.toFloat()
)

fun List<CityDto>.toEntities(): List<City> {
    val cities = mutableListOf<City>()
    forEach {
        try {
            cities.add(it.toEntity())
        } catch (_: Exception) {
        }
    }
    return cities
}