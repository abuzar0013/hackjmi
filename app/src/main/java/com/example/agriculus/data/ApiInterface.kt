package com.example.agriculus.data

import com.example.agriculus.data.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    // https://api.openweathermap.org/data/2.5/weather?q=new delhi,in&units=metric&appid=d1d76e6f74c25f60b602ac34a75cc0e5

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("c") country: String,
        @Query("units") units: String,
        @Query("appid") appikey: String,
    ): Response<CurrentWeather>

}