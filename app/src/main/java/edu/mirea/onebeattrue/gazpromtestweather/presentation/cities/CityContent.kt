package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.gazpromtestweather.R
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.buttonStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.errorStyle
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.stickyHeaderStyle

@Composable
fun CityContent(
    modifier: Modifier = Modifier,
    component: CityComponent
) {
    val state by component.model.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val screenState = state.screenState) {
            CityStore.State.ScreenState.Initial -> {}

            CityStore.State.ScreenState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error_cities),
                        style = errorStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { component.onUpdateClick() }) {
                        Text(
                            text = stringResource(R.string.update),
                            style = buttonStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            is CityStore.State.ScreenState.Loaded -> {
                ListWithHeaders(
                    modifier = modifier.fillMaxSize(),
                    cities = screenState.cities,
                    onClickCity = { city ->
                        component.onCityItemClick(city)
                    }
                )
            }

            CityStore.State.ScreenState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: City,
    showHeader: Boolean,
    onClickCity: (City) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showHeader) {
            Header(
                char = city.name.first().toString(),
                modifier = modifier
            )
        } else {
            Spacer(modifier = modifier.width(56.dp))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(
                topStart = 8.dp,
                bottomStart = 8.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
            onClick = {
                onClickCity(city)
            }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = city.name,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    char: String
) {
    Text(
        modifier = modifier
            .padding(16.dp),
        text = char,
        style = stickyHeaderStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
private fun ListWithHeaders(
    modifier: Modifier = Modifier,
    cities: List<City>,
    onClickCity: (City) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val groupedNames = remember(cities) {
            cities.groupBy { it.name.first() }
        }
        val startIndexes = remember(cities) {
            getStartIndexes(groupedNames.entries)
        }
        val endIndexes = remember(cities) {
            getEndIndexes(groupedNames.entries)
        }

        val commonModifier = Modifier.width(56.dp)
        val listState = rememberLazyListState()
        val moveStickyHeader by remember {
            derivedStateOf {
                endIndexes.contains(listState.firstVisibleItemIndex + 1)
            }
        }

        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 32.dp),
            state = listState
        ) {
            itemsIndexed(
                items = cities,
                key = { _, item ->
                    item.id
                }
            ) { index, city ->
                CityCard(
                    modifier = commonModifier,
                    city = city,
                    showHeader = startIndexes.contains(index) && remember {
                        derivedStateOf { listState.firstVisibleItemIndex }
                    }.value != index,
                ) {
                    onClickCity(it)
                }
            }
        }
        Header(
            modifier = if (moveStickyHeader) {
                Modifier.offset {
                    IntOffset(0, -listState.firstVisibleItemScrollOffset)
                }
            } else {
                Modifier
            },
            char = cities[remember {
                derivedStateOf { listState.firstVisibleItemIndex }
            }.value].name.first()
                .toString(),
        )
    }
}

private fun getStartIndexes(entries: Set<Map.Entry<Char, List<City>>>): List<Int> {
    var currentIndex = 0
    val indexes = mutableListOf<Int>()
    entries.forEach { entry ->
        indexes.add(currentIndex)
        currentIndex += entry.value.size
    }
    return indexes
}

private fun getEndIndexes(entries: Set<Map.Entry<Char, List<City>>>): List<Int> {
    var currentIndex = 0
    val indexes = mutableListOf<Int>()
    entries.forEach { entry ->
        currentIndex += entry.value.size
        indexes.add(currentIndex)
    }
    return indexes
}
