package com.example.myapplication.network

import com.example.myapplication.model.CarInfo
import com.example.myapplication.model.CarLogo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =
    "https://raw.githubusercontent.com/ElyesDer/Vehicule-data-DB/master/"
private const val BASE_URL_LOGOS =
    "https://raw.githubusercontent.com/ErCrasher27/carl-maker-logos.json/main/"

var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL)
        .build()

private val retrofitLogos =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL_LOGOS)
        .build()

interface CarApiService {

    @Headers("Content-Type: application/json")
    @GET("jsondata.json")
    suspend fun getCarInfo(): List<CarInfo>

    @Headers("Content-Type: application/json")
    @GET("carl-maker-logos.json")
    suspend fun getCarLogos(): List<CarLogo>
}

object VehicleApi {
    val retrofitService: CarApiService by lazy { retrofit.create(CarApiService::class.java) }
    val retrofitServiceLogos: CarApiService by lazy { retrofitLogos.create(CarApiService::class.java) }
}