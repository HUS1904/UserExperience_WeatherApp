package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dk.shape.dtu.weatherApp.model.data.WeatherResponse

@Composable
fun CurrentTemp(weatherData: WeatherResponse?){
    val currentTemp = weatherData?.list?.get(0)?.main?.temp
    val condition = weatherData?.list?.firstOrNull()?.weather?.firstOrNull()?.description

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${currentTemp?.minus(273.15)?.toInt() ?: "--"}",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 70.sp
            )
            Text(
                text = "°",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFFE2376C),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 70.sp
            )
        }

        Row {
            Text(
                text = "$condition".replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
        }
    }
}