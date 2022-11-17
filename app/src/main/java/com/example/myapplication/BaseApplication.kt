package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.CarDatabase

class BaseApplication : Application() {
    val database: CarDatabase by lazy { CarDatabase.getDatabase(this) }
}