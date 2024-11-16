package dk.shape.dtu.weatherApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import com.google.android.gms.location.*
import dk.shape.dtu.weatherApp.model.data.WeatherResponse
import dk.shape.dtu.weatherApp.view.CitiesListScreen
import dk.shape.dtu.weatherApp.viewModel.AppContent

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude by mutableStateOf<Double?>(null)
    private var longitude by mutableStateOf<Double?>(null)
    private val citiesWeather = mutableStateListOf<WeatherResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLastKnownLocation()
            } else {
                latitude = 55.6761
                longitude = 12.5683
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "weatherScreen") {
                composable("weatherScreen") {
                    val lat = latitude
                    val lon = longitude
                    if (lat != null && lon != null) {
                        AppContent(lat, lon, null, navController) { newCityWeather ->
                            addCityToList(newCityWeather)
                        }
                    }
                }
                composable("citiesListScreen") {
                    CitiesListScreen(navController = navController, citiesWeather = citiesWeather)
                }
                composable("weatherScreen/{cityName}") { backStackEntry ->
                    val cityName = backStackEntry.arguments?.getString("cityName") ?: "Copenhagen"
                    AppContent(null, null, cityName, navController) { newCityWeather ->
                        addCityToList(newCityWeather)
                    }
                }
            }
        }
    }

    private fun addCityToList(newCityWeather: WeatherResponse?) {
        val cityAlreadyAdded = citiesWeather.any { it.city?.name == newCityWeather?.city?.name }
        if (newCityWeather != null && !cityAlreadyAdded) {
            citiesWeather.add(newCityWeather)
        }
    }

    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            } else {
                latitude = 55.6761
                longitude = 12.5683
            }
        }.addOnFailureListener {
            latitude = 55.6761
            longitude = 12.5683
        }
    }
}