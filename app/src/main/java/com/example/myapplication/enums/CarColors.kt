package com.example.myapplication.enums

import android.graphics.Color

enum class CarColors(
    val nameColor: String,
    val rgbColor: Int
){
    METALLIC_RED("Metallic Red", Color.rgb(192, 14, 26)),
    METALLIC_BLACK("Metallic Black", Color.rgb(13, 17, 22)),
    METALLIC_BRONZE("Metallic Bronze", Color.rgb(145, 101, 50)),
    METALLIC_TORINO_RED("Metallic Torino Red", Color.rgb(218, 25, 24)),
    UTIL_SILVER("Util Silver", Color.rgb(140, 144, 149)),
    MATTE_DARK_RED("Matte Dark Red", Color.rgb(115, 32, 33)),
    MATTE_RED("Matte Red", Color.rgb(207, 31, 33)),
    MATTE_DARK_BLUE("Matte Dark Blue", Color.rgb(31, 40, 82)),
    MATTE_BLUE("Matte Blue", Color.rgb(37, 58, 167)),
    MATTE_LIME_GREEN("Matte Lime Green", Color.rgb(102, 184, 31)),
    MATTE_ORANGE("Matte Orange", Color.rgb(242, 125, 32)),
    MATTE_PURPLE("Matte Purple", Color.rgb(107, 31, 123)),
    MATTE_WHITE("Matte White", Color.rgb(	252, 249, 241)),
    MATTE_YELLOW("Matte Yellow", Color.rgb(255, 201, 31)),
    MATTE_BROWN("Matte Brown", Color.rgb(101, 63, 35)),
    MATTE_GRAY("Matte Gray", Color.rgb(151, 154, 151))
}