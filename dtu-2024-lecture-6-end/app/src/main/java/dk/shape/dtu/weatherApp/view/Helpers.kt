package dk.shape.dtu.weatherApp.view

import androidx.compose.runtime.Composable
import dk.shape.dtu.stateincompose.R
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

fun formatDay(timestamp: Long?): String {
    if (timestamp == null) return "--"
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("EEE", Locale.getDefault())
    return format.format(date)
}

fun formatTimeFromTimestamp(timestamp: Long, timezoneOffset: Int): String {
    val adjustedTimestamp = (timestamp + timezoneOffset) * 1000
    val date = Date(adjustedTimestamp)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

@Composable
fun getCurrentWeatherIconResource(
    description: String?,
    weatherData: WeatherResponse?,
    sunrise: Long,
    sunset: Long
): Int {
    val timestamp = weatherData?.list?.firstOrNull()?.dt
    val currentTime = timestamp ?: (System.currentTimeMillis() / 1000)

    return when {
        description?.contains("thunderstorm", ignoreCase = true) == true -> R.drawable.nbackstorm
        description?.contains("fog", ignoreCase = true) == true -> R.drawable.nbackfog
        description?.contains("snow", ignoreCase = true) == true -> R.drawable.nbacksnow
        description?.contains("cloud", ignoreCase = true) == true -> R.drawable.nbackcloud
        description?.contains("sky", ignoreCase = true) == true -> {if (currentTime in sunrise until sunset) R.drawable.nbacksun else R.drawable.nbackmoon}
        description?.contains("drizzle", ignoreCase = true) == true -> R.drawable.nbackrain
        description?.contains("rain", ignoreCase = true) == true -> R.drawable.nbackrain
        else -> R.drawable.nbackcloud
    }
}

fun getCityTime(timezoneOffset: Int): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.add(Calendar.SECOND, timezoneOffset)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(calendar.time)
}