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

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL)
        .build()

/**
 * A public interface that exposes the [getVehicleInfo] method
 */
interface CarApiService {
    /**
     * Returns a [List] of [VehicleInfo] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "year" endpoint will be requested with the GET
     * HTTP method
     */

    @Headers("Content-Type: application/json")
    @GET("jsondata.json")
    suspend fun getCarInfo(): List<CarInfo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object VehicleApi {
    val retrofitService: CarApiService by lazy { retrofit.create(CarApiService::class.java) }
}