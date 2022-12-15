package com.example.myapplication.model


import com.google.gson.annotations.SerializedName

data class CarInfo(
    @SerializedName("YEAR") val year: String,
    @SerializedName("MAKER") val maker: String,
    @SerializedName("MODEL") val model: String,
    @SerializedName("FULLMODELNAME") val fullModelName: String,
    @SerializedName("PRODUCTION END") val production_end: String,
)

