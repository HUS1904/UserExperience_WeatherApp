package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import kotlin.math.ceil

@Composable
fun CityItem(
    weatherResponse: WeatherResponse,
    uvIndex: Double?,
    onCityClick: (String) -> Unit
) {
    val city = weatherResponse.city?.name ?: "Unknown City"
    val country = weatherResponse.city?.country ?: "Unknown Country"
    val description = weatherResponse.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "No data"
    val humidity = weatherResponse.list.firstOrNull()?.main?.humidity ?: 0
    val rainVolume = weatherResponse.list.firstOrNull()?.rain?.last3Hours ?: 0.0
    val time = getCityTime(weatherResponse.city?.timezone ?: 0)
    val currentTemp = weatherResponse.list.firstOrNull()?.main?.temp?.minus(273.15)?.let { ceil(it).toInt() }
    val iconResource = getCurrentWeatherIconResource(
        description = description,
        weatherData = weatherResponse,
        sunrise = weatherResponse.city?.sunrise?.toLong() ?: 0L,
        sunset = weatherResponse.city?.sunset?.toLong() ?: 0L
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 5.dp).clickable { onCityClick(city) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF383838)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f).height(100.dp)
            ) {
                Image(
                    painter = painterResource(id = iconResource),
                    contentDescription = description,
                    modifier = Modifier.fillMaxSize().alpha(0.4f)
                )

                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "$city, $country",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Time: $time",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "${currentTemp ?: "--"}°, $description",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "Humidity: $humidity%", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text(text = "Rain: ${"%.1f".format(rainVolume)} mm", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text(text = "UV Index: ${"%.1f".format(uvIndex ?: 0.0)}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
        }
    }
}

