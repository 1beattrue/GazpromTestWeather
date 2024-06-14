package edu.mirea.onebeattrue.gazpromtestweather.presentation.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    component: WeatherComponent
) {
    val state by component.model.collectAsState()


}