package com.example.myapplication.utils

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
        Picasso.get()
            .load(logoDataApiMap[brand.lowercase()])
            .into(logo)
    } else {
        Picasso.get()
            .load(R.drawable.ic_seats_24)
            .into(logo)
        logo.alpha = 1.0F
    }
}