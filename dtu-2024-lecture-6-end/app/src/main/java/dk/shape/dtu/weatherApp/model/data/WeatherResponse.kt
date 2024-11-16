package dk.shape.dtu.weatherApp.model.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var list: ArrayList<WeatherData> = arrayListOf(),
    @SerializedName("city") var city: City? = City()
)

data class City(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("country") var country: String? = null,
    @SerializedName("population") var population: Int? = null,
    @SerializedName("timezone") var timezone: Int? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null
)

data class Coord(
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null
)

data class WeatherData(
    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("sys") var sys: Sys? = Sys(),
    @SerializedName("dt_txt") var dtTxt: String? = null,
    @SerializedName("rain") var rain: Rain? = null
)

data class Rain(
    @SerializedName("3h") var last3Hours: Double? = null
)

data class Main(
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("temp_min") var tempMin: Double? = null,
    @SerializedName("temp_max") var tempMax: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("sea_level") var seaLevel: Int? = null,
    @SerializedName("grnd_level") var grndLevel: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("temp_kf") var tempKf: Double? = null
)

data class Weather(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("main") var main: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("icon") var icon: String? = null
)

data class Clouds(
    @SerializedName("all") var all: Int? = null
)

data class Wind(
    @SerializedName("speed") var speed: Double? = null,
    @SerializedName("deg") var deg: Int? = null,
    @SerializedName("gust") var gust: Double? = null
)

data class Sys(
    @SerializedName("pod") var pod: String? = null
)

data class UvIndexResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("date_iso") val dateIso: String,
    @SerializedName("date") val date: Long,
    @SerializedName("value") val value: Double
)