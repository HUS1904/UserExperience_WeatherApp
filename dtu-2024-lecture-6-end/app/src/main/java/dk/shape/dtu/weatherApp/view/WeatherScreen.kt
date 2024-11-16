package dk.shape.dtu.weatherApp.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dk.shape.dtu.weatherApp.model.data.WeatherResponse

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun WeatherScreen(navController: NavController, weatherData: WeatherResponse?, uvIndex: Double?) {
    val imageOffsetX = remember { Animatable(300f) }

    LaunchedEffect(Unit) {
        imageOffsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = LinearOutSlowInEasing
            )
        )
    }

    val sunrise = weatherData?.city?.sunrise?.toLong() ?: 0L
    val sunset = weatherData?.city?.sunset?.toLong() ?: 0L

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.navigate("citiesListScreen") },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color(0xFFE2376C)
            )
        }

        if (weatherData == null) {
            Text(text = "Loading weather data...", modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp).background(Color.Transparent)
                    ) {
                        Image(
                            painter = painterResource(
                                id = getCurrentWeatherIconResource(
                                    description = weatherData.list.firstOrNull()?.weather?.firstOrNull()?.description,
                                    weatherData = weatherData,
                                    sunrise = sunrise,
                                    sunset = sunset
                                )
                            ),
                            contentDescription = "Weather background",
                            modifier = Modifier.fillMaxSize().alpha(0.4f).offset(x = imageOffsetX.value.dp).align(Alignment.Center)
                        )

                        Column(
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CityCountry(weatherData)
                            }
                            CurrentTemp(weatherData)
                        }
                    }
                }

                item { SunTimes(weatherData) }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    HourlyForecast(
                        forecasts = weatherData.list,
                        weatherData = weatherData
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item { WeeklyForecast(forecasts = weatherData.list) }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Rain(weatherData = weatherData, modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(16.dp))

                        FeelsLike(weatherData = weatherData, modifier = Modifier.weight(1f))
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        AirHumidity(weatherData = weatherData, modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(16.dp))

                        UvDisplay(uvIndex, modifier = Modifier.weight(1f))
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        WindAndDirectionRow(weatherData, modifier = Modifier.fillMaxWidth())
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}