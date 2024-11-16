package dk.shape.dtu.weatherApp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import dk.shape.dtu.stateincompose.R
import dk.shape.dtu.weatherApp.model.data.WeatherResponse

@SuppressLint("DefaultLocale")
@Composable
fun Rain(weatherData: WeatherResponse?, modifier: Modifier = Modifier) {
    val rainLast24Hours = weatherData?.list?.take(8)?.sumOf { it.rain?.last3Hours ?: 0.0 } ?: 0.0
    val formattedrainLast24Hours = String.format("%.1f", rainLast24Hours)
    val rainNext24Hours = weatherData?.list?.drop(8)?.take(8)?.sumOf { it.rain?.last3Hours ?: 0.0 } ?: 0.0
    val formattedRainNext24Hours = String.format("%.1f", rainNext24Hours)

    Column(
        modifier = modifier.background(Color(0xFF383838), shape = RoundedCornerShape(10.dp)).padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.drops),
                contentDescription = "Raindrop Icon",
                tint = Color(0xFFE2376C),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "RAIN",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "${formattedrainLast24Hours} mm",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "In the last 24 hours",
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${formattedRainNext24Hours} mm of rain is expected\nin the next 24 hours",
            color = Color.LightGray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun FeelsLike(weatherData: WeatherResponse?, modifier: Modifier = Modifier){
    val FeelsLike = weatherData?.list?.get(0)?.main?.feelsLike

    Column(
        modifier = modifier.background(Color(0xFF383838), shape = RoundedCornerShape(10.dp)).padding(16.dp),
        horizontalAlignment = Alignment.Start
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.feels),
                contentDescription = "Feels like Icon",
                tint = Color(0xFFE2376C),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "FEELS LIKE",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row () {
            Text(
                text = "${FeelsLike?.minus(273.15)?.toInt() ?: "--"}",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
            Text(
                text = "°",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFFE2376C),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Wind makes it feel cooler",
            color = Color.LightGray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun AirHumidity(weatherData: WeatherResponse?, modifier: Modifier = Modifier) {
    val airHumidity = weatherData?.list?.get(0)?.main?.humidity ?: 0
    val currentTempKelvin = weatherData?.list?.get(0)?.main?.temp ?: 0.0
    val currentTempCelsius = currentTempKelvin - 273.15
    val a = 17.27
    val b = 237.7
    val alpha = (a * currentTempCelsius) / (b + currentTempCelsius) + kotlin.math.ln(airHumidity / 100.0)
    val dewPoint = (b * alpha) / (a - alpha)

    Column(
        modifier = modifier.background(Color(0xFF383838), shape = RoundedCornerShape(10.dp)).padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.humidity),
                contentDescription = "Humidity Icon",
                tint = Color(0xFFE2376C),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "AIR HUMIDITY",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = "$airHumidity",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
            Text(
                text = "%",
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFFE2376C),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The dew point is ${"%.1f".format(dewPoint)}° right now",
            color = Color.LightGray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun UvDisplay(uvIndex: Double?, modifier: Modifier = Modifier) {
    val uvLevel = when {
        uvIndex == null -> "Unknown"
        uvIndex <= 2 -> "Low"
        uvIndex <= 5 -> "Moderate"
        uvIndex <= 7 -> "High"
        uvIndex <= 10 -> "Very High"
        else -> "Extreme"
    }

    val uvColor = when (uvLevel) {
        "Low" -> Color(0xFF4CAF50)
        "Moderate" -> Color(0xFFFFEB3B)
        "High" -> Color(0xFFFF9800)
        "Very High" -> Color(0xFFF44336)
        "Extreme" -> Color(0xFF9C27B0)
        else -> Color.Gray
    }

    Column(
        modifier = modifier.background(Color(0xFF383838), shape = RoundedCornerShape(10.dp)).padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "UV Index Icon",
                tint = Color(0xFFE2376C),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "UV INDEX",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Text(
            text = uvIndex?.toString() ?: "--",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Text(
            text = uvLevel,
            color = uvColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(11.dp).background(Color.Gray, shape = RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((uvIndex?.coerceIn(0.0, 11.0)?.div(11.0) ?: 0.0).toFloat())
                    .background(uvColor, shape = RoundedCornerShape(10.dp))
            )
        }
    }
}

@Composable
fun WindSpeed(weatherData: WeatherResponse?, modifier: Modifier = Modifier) {
    val speed = weatherData?.list?.get(0)?.wind?.speed ?: 0.0
    val direction = weatherData?.list?.get(0)?.wind?.deg ?: 0
    val gust = weatherData?.list?.get(0)?.wind?.gust ?: 0.0

    Column(
        modifier = modifier.background(Color(0xFF383838),
        shape = RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Wind",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$speed m/s",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Wind gust",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$gust m/s",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Wind direction",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$direction°",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun DirectionalWidget(weatherData: WeatherResponse?, modifier: Modifier = Modifier) {
    val speed = weatherData?.list?.get(0)?.wind?.speed ?: 0.0
    val direction = weatherData?.list?.get(0)?.wind?.deg ?: 0

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(130.dp)
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            drawCircle(
                color = Color(0xFF282828),
                radius = size.minDimension / 2f,
                style = Stroke(width = 15.dp.toPx())
            )
            rotate(degrees = direction.toFloat(), pivot = center) {
                val outerRadius = size.minDimension / 2f
                val arrowDistance = outerRadius + 15f
                val trianglePath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(center.x, center.y - arrowDistance)
                    lineTo(center.x - 15f, center.y - arrowDistance + 25f)
                    lineTo(center.x + 15f, center.y - arrowDistance + 25f)
                    close()
                }
                drawPath(
                    path = trianglePath,
                    color = Color(0xFFE2376C)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$speed",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "m/s",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "N",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-10).dp)
        )
        Text(
            text = "E",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = 2.dp)
        )
        Text(
            text = "S",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (11).dp)
        )
        Text(
            text = "W",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterStart).offset(x = (-8).dp)
        )
    }
}

@Composable
fun WindAndDirectionRow(
    weatherData: WeatherResponse?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color(0xFF383838), shape = RoundedCornerShape(10.dp)).padding(16.dp).fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.air),
                contentDescription = "Wind Icon",
                tint = Color(0xFFE2376C),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "WIND",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WindSpeed(
                weatherData = weatherData,
                modifier = Modifier.weight(1f)
            )

            DirectionalWidget(
                weatherData = weatherData,
                modifier = Modifier.weight(1f).padding(start = 40.dp, bottom = 10.dp)
            )
        }
    }
}