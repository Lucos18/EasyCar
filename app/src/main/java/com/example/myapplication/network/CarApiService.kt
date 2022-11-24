package com.example.myapplication.network

import com.example.myapplication.model.CarInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =
    "https://raw.githubusercontent.com/ElyesDer/Vehicule-data-DB/master/"

var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL)
        .build()

interface CarApiService {

    @Headers("Content-Type: application/json")
    @GET("jsondata.json")
    suspend fun getCarInfo(): List<CarInfo>
}

object VehicleApi {
    val retrofitService: CarApiService by lazy { retrofit.create(CarApiService::class.java) }
}