package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.gazpromtestweather.R
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Forecast
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import edu.mirea.onebeattrue.gazpromtestweather.presentation.extensions.formattedFullDate
import edu.mirea.onebeattrue.gazpromtestweather.presentation.extensions.formattedShortDate
import edu.mirea.onebeattrue.gazpromtestweather.presentation.extensions.tempToFormattedString
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.buttonStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.dateStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.errorStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.subtitleStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.titleStyle

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    component: WeatherComponent
) {
    val state by component.model.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 40.dp,
                bottom = 36.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            when (val weatherState = state.weatherState) {
                WeatherStore.State.WeatherState.Initial -> {}

                WeatherStore.State.WeatherState.Error -> {
                    Text(
                        text = stringResource(R.string.error_weather),
                        style = errorStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                is WeatherStore.State.WeatherState.Loaded -> {
                    WeatherCard(
                        weather = weatherState.weather,
                        city = component.city
                    )
                }

                WeatherStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
            }
        }

        item {
            when (val forecastState = state.forecastState) {
                WeatherStore.State.ForecastState.Initial -> {}

                WeatherStore.State.ForecastState.Error -> {
                    Text(
                        text = stringResource(R.string.error_forecast),
                        style = errorStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                is WeatherStore.State.ForecastState.Loaded -> {
                    ForecastCard(forecast = forecastState.forecast)
                }

                WeatherStore.State.ForecastState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
            }
        }

        item {
            Button(
                onClick = { component.onUpdateClick() }
            ) {
                Text(
                    text = stringResource(R.string.update),
                    style = buttonStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    weather: Weather,
    city: City
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.temp.tempToFormattedString(),
                style = titleStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = city.name,
                style = subtitleStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = weather.date.formattedFullDate(),
                style = dateStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            GlideImage(
                modifier = Modifier.size(100.dp),
                model = weather.iconUrl,
                contentDescription = null
            )
            Text(
                text = weather.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ForecastCard(
    modifier: Modifier = Modifier,
    forecast: Forecast,
) {
    LazyRow(
        modifier = modifier
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(forecast.upcoming) { weather ->
            WeatherItem(weather = weather)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherItem(
    modifier: Modifier = Modifier,
    weather: Weather,
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(min = 132.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.date.formattedShortDate(),
                style = dateStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = weather.temp.tempToFormattedString(),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            GlideImage(
                modifier = Modifier.size(100.dp),
                model = weather.iconUrl,
                contentDescription = null
            )
            Text(
                text = weather.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}