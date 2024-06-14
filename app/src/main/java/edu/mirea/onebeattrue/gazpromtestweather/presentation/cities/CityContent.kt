package edu.mirea.onebeattrue.gazpromtestweather.presentation.cities

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.gazpromtestweather.domain.entity.City
import edu.mirea.onebeattrue.gazpromtestweather.ui.theme.stickyHeader

@Composable
fun CityContent(
    modifier: Modifier = Modifier,
    component: CityComponent
) {
    val state by component.model.collectAsState()


    when (val screenState = state.screenState) {
        CityStore.State.ScreenState.Error -> {

        }

        CityStore.State.ScreenState.Initial -> {

        }

        is CityStore.State.ScreenState.Loaded -> {
            CategorizedLazyColumn(
                modifier = modifier.fillMaxSize(),
                categories = screenState.cities,
                onClickCity = { city ->
                    component.onCityItemClick(city)
                }
            )
        }

        CityStore.State.ScreenState.Loading -> {

        }
    }
}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: City,
    onClickCity: (City) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 56.dp),
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

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            style = stickyHeader,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        HorizontalDivider()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategorizedLazyColumn(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onClickCity: (City) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        categories.forEach { category ->
            stickyHeader {
                Header(text = category.name)
            }

            items(
                items = category.items,
                key = { it.id }
            ) { city: City ->
                CityCard(city = city) {
                    onClickCity(it)
                }
            }
        }
    }
}