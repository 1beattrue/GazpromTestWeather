package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import edu.mirea.onebeattrue.gazpromtestweather.R
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.Weather
import edu.mirea.onebeattrue.gazpromtestweather.presentation.extensions.tempToFormattedString
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.buttonStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.errorStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.subtitleStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.titleStyle

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    component: WeatherComponent
) {
    val state by component.model.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 40.dp,
                bottom = 36.dp
            )
    ) {
        when (val weatherState = state.weatherState) {
            WeatherStore.State.WeatherState.Initial -> {}

            WeatherStore.State.WeatherState.Error -> {
                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = stringResource(R.string.error),
                    style = errorStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            is WeatherStore.State.WeatherState.Loaded -> {
                WeatherCard(
                    modifier = modifier.align(Alignment.TopCenter),
                    weather = weatherState.weather,
                    city = component.city
                )
            }

            WeatherStore.State.WeatherState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopCenter),
                )
            }
        }

        when (val forecastState = state.forecastState) {
            WeatherStore.State.ForecastState.Initial -> {}

            WeatherStore.State.ForecastState.Error -> {

            }

            is WeatherStore.State.ForecastState.Loaded -> {

            }

            WeatherStore.State.ForecastState.Loading -> {

            }
        }

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
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

@Composable
private fun WeatherCard(
    modifier: Modifier = Modifier,
    weather: Weather,
    city: City
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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
        }
    }
}