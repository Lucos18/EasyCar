package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CarLogo(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
)
