package dk.shape.dtu.weatherApp.viewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import dk.shape.dtu.weatherApp.model.data.RetrofitInstance
import dk.shape.dtu.weatherApp.model.data.UvIndexResponse
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import dk.shape.dtu.weatherApp.view.WeatherScreen
import retrofit2.*

private val apiKey = "d63ace15f43fa9ad250fcd0b88a420cd"

@Composable
fun AppContent(
    latitude: Double?,
    longitude: Double?,
    cityName: String?,
    navController: NavController,
    onWeatherFetched: (WeatherResponse?) -> Unit
) {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var uvIndex by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(latitude, longitude, cityName) {
        if (latitude != null && longitude != null) {
            fetchWeatherDataByCoordinates(latitude, longitude) { data ->
                weatherData = data
                onWeatherFetched(data)
                data?.city?.coord?.let { coord ->
                    fetchUvIndex(coord.lat ?: 0.0, coord.lon ?: 0.0) { uv ->
                        uvIndex = uv
                    }
                }
            }
        } else if (cityName != null) {
            fetchWeatherData(cityName) { data ->
                weatherData = data
                onWeatherFetched(data)
                data?.city?.coord?.let { coord ->
                    fetchUvIndex(coord.lat ?: 0.0, coord.lon ?: 0.0) { uv ->
                        uvIndex = uv
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF282828)
    ) {
        Column {
            WeatherScreen(navController, weatherData = weatherData, uvIndex = uvIndex)
        }
    }
}

fun fetchWeatherDataByCoordinates(lat: Double, lon: Double, onResult: (WeatherResponse?) -> Unit) {
    val call = RetrofitInstance.api.getWeatherByCoordinates(lat, lon, apiKey)

    call.enqueue(object : Callback<WeatherResponse> {
        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }

        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            onResult(null)
        }
    })
}

fun fetchWeatherData(city: String, onResult: (WeatherResponse?) -> Unit) {
    val call = RetrofitInstance.api.getWeather(city, apiKey)

    call.enqueue(object : Callback<WeatherResponse> {
        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
            if (response.isSuccessful) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }

        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            onResult(null)
        }
    })
}

fun fetchUvIndex(lat: Double, lon: Double, onResult: (Double?) -> Unit) {
    val call = RetrofitInstance.api.getUvIndex(lat, lon, apiKey)

    call.enqueue(object : Callback<UvIndexResponse> {
        override fun onResponse(call: Call<UvIndexResponse>, response: Response<UvIndexResponse>) {
            if (response.isSuccessful) {
                val uvIndex = response.body()?.value
                onResult(uvIndex)
            } else {
                onResult(null)
            }
        }

        override fun onFailure(call: Call<UvIndexResponse>, t: Throwable) {
            onResult(null)
        }
    })
}
