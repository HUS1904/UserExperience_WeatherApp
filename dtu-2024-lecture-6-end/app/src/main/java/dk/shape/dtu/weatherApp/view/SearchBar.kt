package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import kotlin.math.ceil

@Composable
fun CitySearchResult(
    weatherResponse: WeatherResponse,
    onAddCity: (() -> Unit)? = null,
    isPreview: Boolean = false
) {
    val city = weatherResponse.city?.name ?: "Unknown City"
    val country = weatherResponse.city?.country ?: "Unknown Country"
    val description = weatherResponse.list.firstOrNull()?.weather?.firstOrNull()?.description ?: "No data"
    val currentTemp = weatherResponse.list.firstOrNull()?.main?.temp?.minus(273.15)?.let { ceil(it).toInt() }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF383838))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$city, $country",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "${currentTemp ?: "--"}°, $description",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (isPreview) {
                Text(
                    text = "Add",
                    color = Color(0xFFE2376C),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp).clickable { onAddCity?.invoke() },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}