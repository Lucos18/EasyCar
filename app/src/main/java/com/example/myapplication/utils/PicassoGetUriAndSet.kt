package com.example.myapplication.utils

import android.media.tv.TvContract
import android.util.Log
import android.widget.ImageView
import com.example.myapplication.R
import com.example.myapplication.model.CarLogo
import com.squareup.picasso.Picasso

fun setAndGetUriByBrandParsingListOfLogoAndImageView(
    logoDataApi: List<CarLogo>?,
    brand: String,
    logo: ImageView
) {
    val logoDataApiMap = logoDataApi?.associate { it.name.lowercase() to it.logo }
    if (logoDataApiMap?.get(brand.lowercase()) != null) {
        Log.d("picasso", brand)
        Picasso.get()
            .load(logoDataApiMap[brand.lowercase()])
            .into(logo)
    }
}