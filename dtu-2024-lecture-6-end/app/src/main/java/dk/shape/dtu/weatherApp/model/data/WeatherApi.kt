package dk.shape.dtu.weatherApp.model.data

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface WeatherApi {
    @GET("forecast")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    @GET("forecast")
    fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>

    @GET("uvi")
    fun getUvIndex(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<UvIndexResponse>
}