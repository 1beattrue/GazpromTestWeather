package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun CityContent(
    modifier: Modifier = Modifier,
    component: CityComponent
) {
    val state by component.model.collectAsState()


}