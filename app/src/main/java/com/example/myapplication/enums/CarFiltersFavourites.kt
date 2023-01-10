package com.example.myapplication.enums

enum class CarFiltersFavourites(
    val nameFilter: String,
    var isSelected: Boolean
) {
    PRICE_ASCENDING_ORDER("Ascending Order Price", false),
    PRICE_DESCENDING_ORDER("Descending Order Price", false),
    YEAR_ASCENDING_ORDER("Ascending Order Year", false),
    YEAR_DESCENDING_ORDER("Descending Order Year", false),
    MILEAGE_ASCENDING_ORDER("Ascending Order Mileage", false),
    MILEAGE_DESCENDING_ORDER("Descending Order Mileage", false)
}