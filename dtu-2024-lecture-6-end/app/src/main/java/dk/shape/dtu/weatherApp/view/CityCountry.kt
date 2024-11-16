package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CityCountry(weatherData: WeatherResponse?){
    if (weatherData != null) {
        val timestamp = (weatherData?.list?.get(0)?.dt ?: 0L) * 1000
        val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(timestamp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${weatherData.city?.name}, ${weatherData.city?.country}".uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 35.sp
            )

            Text(
                text = day.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )
        }
    }
}