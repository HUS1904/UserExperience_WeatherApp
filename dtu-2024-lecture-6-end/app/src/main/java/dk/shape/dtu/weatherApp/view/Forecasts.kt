package dk.shape.dtu.weatherApp.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import dk.shape.dtu.stateincompose.R
import dk.shape.dtu.weatherApp.model.data.WeatherData
import dk.shape.dtu.weatherApp.model.data.WeatherResponse

data class DailyForecast(
    val day: String,
    val iconRes: Int,
    val minTemp: Float,
    val maxTemp: Float
)

@Composable
fun HourlyForecast(
    forecasts: List<WeatherData>,
    weatherData: WeatherResponse?
) {
    val timezoneOffset = weatherData?.city?.timezone ?: 0
    val sunrise = weatherData?.city?.sunrise?.toLong() ?: 0L
    val sunset = weatherData?.city?.sunset?.toLong() ?: 0L
    val currentTimestamp = System.currentTimeMillis() / 1000
    val filteredForecasts = forecasts.filter { (it.dt ?: 0L) >= currentTimestamp }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Hourly Forecast",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(filteredForecasts.take(20)) { index, forecast ->
                Column(
                    modifier = Modifier
                        .width(75.dp)
                        .height(130.dp)
                        .background(
                            color = if (index == 0) Color(0xFFE2376C) else Color(0xFF383838),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatTimeFromTimestamp(forecast.dt ?: 0L, timezoneOffset),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Image(
                        painter = painterResource(
                            id = getHourlyWeatherIconResource(
                                description = forecast.weather.firstOrNull()?.description,
                                forecastTime = forecast.dt ?: 0L,
                                sunrise = sunrise,
                                sunset = sunset
                            )
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = forecast.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "Unknown",
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${(forecast.main?.temp?.minus(273.15))?.toInt() ?: "--"}°C",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun getHourlyWeatherIconResource(
    description: String?,
    forecastTime: Long,
    sunrise: Long,
    sunset: Long
): Int {
    return when {
        description?.contains("thunderstorm", ignoreCase = true) == true -> R.drawable.thunderstorm
        description?.contains("fog", ignoreCase = true) == true -> R.drawable.fog
        description?.contains("snow", ignoreCase = true) == true -> R.drawable.snow
        description?.contains("cloud", ignoreCase = true) == true -> R.drawable.cloud
        description?.contains("sky", ignoreCase = true) == true -> {
            if (forecastTime in sunrise until sunset) R.drawable.sun else R.drawable.moon
        }
        description?.contains("drizzle", ignoreCase = true) == true -> R.drawable.drizzle
        description?.contains("rain", ignoreCase = true) == true -> R.drawable.rain
        else -> R.drawable.cloud
    }
}

@Composable
fun WeeklyForecast(forecasts: List<WeatherData>) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Weekly Forecast",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        val dailyForecasts = forecasts.groupBy { formatDay(it.dt) }
            .map { (day, dailyData) ->
                val minTemp = dailyData.mapNotNull { it.main?.tempMin?.minus(273.15)?.toFloat() }.minOrNull() ?: Float.MAX_VALUE
                val maxTemp = dailyData.mapNotNull { it.main?.tempMax?.minus(273.15)?.toFloat() }.maxOrNull() ?: Float.MIN_VALUE
                val description = dailyData.firstOrNull()?.weather?.firstOrNull()?.description
                val iconRes = getWeeklyWeatherIconResource(description)

                DailyForecast(day, iconRes, minTemp, maxTemp)
            }
            .take(7)

        val globalMinTemp = dailyForecasts.minOfOrNull { it.minTemp } ?: 0f
        val globalMaxTemp = dailyForecasts.maxOfOrNull { it.maxTemp } ?: 1f
        val globalTempRange = globalMaxTemp - globalMinTemp

        dailyForecasts.forEachIndexed { index, forecast ->
            val isFirstItem = index == 0
            val isLastItem = index == dailyForecasts.lastIndex
            val shape = when {
                isFirstItem -> RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                isLastItem -> RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                else -> RoundedCornerShape(0.dp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = Color(0xFF383838), shape = shape)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = forecast.day,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(50.dp)
                )
                Image(
                    painter = painterResource(id = forecast.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${forecast.minTemp.toInt()}°",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                    Box(
                        modifier = Modifier.width(130.dp).height(8.dp).background(Color(0xFF4e4e4e), shape = RoundedCornerShape(4.dp))
                    ) {
                        val minOffsetFraction = (forecast.minTemp - globalMinTemp) / globalTempRange
                        val widthFraction = (forecast.maxTemp - forecast.minTemp) / globalTempRange

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(widthFraction)
                                .align(Alignment.CenterStart)
                                .offset(x = minOffsetFraction * 130.dp)
                                .background(Color(0xFFE2376C), shape = RoundedCornerShape(4.dp))
                        )
                    }
                    Text(
                        text = "${forecast.maxTemp.toInt()}°",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun getWeeklyWeatherIconResource(description: String?): Int {
    return when {
        description?.contains("thunderstorm", ignoreCase = true) == true -> R.drawable.thunderstorm
        description?.contains("fog", ignoreCase = true) == true -> R.drawable.fog
        description?.contains("snow", ignoreCase = true) == true -> R.drawable.snow
        description?.contains("cloud", ignoreCase = true) == true -> R.drawable.cloud
        description?.contains("sky", ignoreCase = true) == true -> R.drawable.sun
        description?.contains("drizzle", ignoreCase = true) == true -> R.drawable.drizzle
        description?.contains("rain", ignoreCase = true) == true -> R.drawable.rain
        else -> R.drawable.cloud
    }
}