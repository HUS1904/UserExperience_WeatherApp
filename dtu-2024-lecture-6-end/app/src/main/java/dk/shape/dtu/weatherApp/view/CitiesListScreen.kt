package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import dk.shape.dtu.weatherApp.viewModel.fetchWeatherData
import dk.shape.dtu.weatherApp.viewModel.fetchUvIndex
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    navController: NavController,
    citiesWeather: MutableList<WeatherResponse>
) {
    var searchQuery by remember { mutableStateOf("") }
    var previewWeather by remember { mutableStateOf<WeatherResponse?>(null) }
    var previewUvIndex by remember { mutableStateOf<Double?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Saved Cities",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFE2376C))
                    }
                },
                modifier = Modifier.padding(9.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF282828))
            )
        },
        containerColor = Color(0xFF282828)
    ) {
        paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).background(Color(0xFF383838), shape = MaterialTheme.shapes.extraLarge)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for a city", color = Color.Gray, fontSize = 17.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color(0xFFE2376C)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            LaunchedEffect(searchQuery) {
                if (searchQuery.isNotBlank()) {
                    fetchWeatherData(searchQuery) { weatherResponse ->
                        previewWeather = weatherResponse
                        previewWeather?.city?.coord?.let { coord ->
                            fetchUvIndex(coord.lat ?: 0.0, coord.lon ?: 0.0) { uv ->
                                previewUvIndex = uv
                            }
                        }
                    }
                } else {
                    previewWeather = null
                    previewUvIndex = null
                }
            }

            previewWeather?.let { weather ->
                CitySearchResult(
                    weatherResponse = weather,
                    onAddCity = {
                        if (!citiesWeather.any { it.city?.name == weather.city?.name }) {
                            citiesWeather.add(weather)
                            previewWeather = null
                            previewUvIndex = null
                            searchQuery = ""
                        }
                    },
                    isPreview = true
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(citiesWeather) { weatherResponse ->
                    CityItem(
                        weatherResponse = weatherResponse,
                        uvIndex = null,
                        onCityClick = { cityName ->
                            navController.navigate("weatherScreen/$cityName")
                        }
                    )
                }
            }
        }
    }
}


