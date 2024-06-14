package edu.mirea.onebeattrue.gazpromtestweather.data.mapper

import edu.mirea.onebeattrue.gazpromtestweather.data.network.dto.CityDto
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City


fun CityDto.toEntity() = City(
    id = id,
    name = name,
    lat = lat,
    lon = lon
)