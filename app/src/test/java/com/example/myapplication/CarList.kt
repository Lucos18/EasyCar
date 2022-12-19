package com.example.myapplication

import com.example.myapplication.enums.CarColors
import com.example.myapplication.enums.fuelType
import com.example.myapplication.model.Car

val CarList = listOf<Car>(
    Car(
        brand = "ALFA ROMEO",
        model = "Giulia",
        yearStartProduction = 2016,
        yearEndProduction = null,
        carPower = 150,
        seats = 5,
        fuelType = fuelType.Diesel.toString(),
        image = null,
        color = CarColors.MATTE_BLUE.nameColor,
        mileage = 0.0,
        price = 100000.0
    ),
    Car(
        brand = "ACURA",
        model = "ILX",
        yearStartProduction = 2016,
        yearEndProduction = null,
        carPower = 200,
        seats = 5,
        fuelType = fuelType.Petrol.toString(),
        image = null,
        color = CarColors.MATTE_DARK_BLUE.nameColor,
        mileage = 1000.0,
        price = 50000.0
    ),
    Car(
        brand = "BUGATTI",
        model = "Chiron",
        yearStartProduction = 2016,
        yearEndProduction = null,
        carPower = 370,
        seats = 2,
        fuelType = fuelType.Diesel.toString(),
        image = null,
        color = CarColors.MATTE_RED.nameColor,
        mileage = 0.0,
        price = 500000.0
    ),
    Car(
        brand = "CITROEN",
        model = "C3",
        yearStartProduction = 2016,
        yearEndProduction = null,
        carPower = 160,
        seats = 2,
        fuelType = fuelType.PHEV.toString(),
        image = null,
        color = CarColors.MATTE_BLUE.nameColor,
        mileage = 0.0,
        price = 40000.0
    )
)