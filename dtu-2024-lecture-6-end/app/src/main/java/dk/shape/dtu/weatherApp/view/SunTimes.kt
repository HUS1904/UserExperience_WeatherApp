package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import dk.shape.dtu.stateincompose.R
import dk.shape.dtu.weatherApp.model.data.WeatherResponse

@Composable
fun SunTimes(weatherData: WeatherResponse?) {
    val timezoneOffset = weatherData?.city?.timezone ?: 0
    val sunriseTimestamp = weatherData?.city?.sunrise?.toLong() ?: 0L
    val sunsetTimestamp = weatherData?.city?.sunset?.toLong() ?: 0L
    val currentTime = System.currentTimeMillis() / 1000
    val dayDuration = sunsetTimestamp - sunriseTimestamp
    val timeElapsed = currentTime - sunriseTimestamp
    val dayProgress = if (dayDuration > 0) (timeElapsed.toFloat() / dayDuration) else 0f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatTimeFromTimestamp(sunriseTimestamp, timezoneOffset),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Image(
                painter = painterResource(id = R.drawable.sunrise),
                contentDescription = "Sunrise Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(0.6f).height(8.dp).background(Color(0xFF383838), shape = RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(dayProgress).fillMaxHeight().background(Color(0xFFE2376C), shape = RoundedCornerShape(4.dp))
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatTimeFromTimestamp(sunsetTimestamp, timezoneOffset),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Image(
                painter = painterResource(id = R.drawable.sunset),
                contentDescription = "Sunset Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}